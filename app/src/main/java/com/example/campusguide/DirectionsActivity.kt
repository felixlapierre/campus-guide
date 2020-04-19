package com.example.campusguide

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.campusguide.directions.KlaxonDirectionsAPIResponseParser
import com.example.campusguide.directions.PathPolyline
import com.example.campusguide.directions.RoutePreviewActivity
import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.StepsActivity
import com.example.campusguide.directions.TransitRoute
import com.example.campusguide.directions.TransitRouteAdapter
import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.indoor.SelectAccessibilityOptionsDialogFragment
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.request.ApiKeyRequestDecorator
import com.example.campusguide.utils.request.VolleyRequestDispatcher
import com.google.android.gms.maps.SupportMapFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_directions.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DirectionsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var map: GoogleMapAdapter
    private lateinit var start: String
    private lateinit var end: String
    private lateinit var startName: String
    private lateinit var endName: String
    private lateinit var currentPath: PathPolyline
    private lateinit var mainPaths: Map<String, PathPolyline>
    private lateinit var extraPaths: Map<String, PathPolyline>
    private val colorStateList: ColorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ), intArrayOf(
            Color.parseColor(Constants.PRIMARY_COLOR_DARK), // disabled
            Color.WHITE // enabled
        )
    )
    private lateinit var listView: ListView
    private lateinit var adapter: TransitRouteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directions)

        map = GoogleMapAdapter()
        val initializer = GoogleMapInitializer(this, map, "directions_activity_map")

        // Extract origin and destination from the intent
        start = intent.getStringExtra("OriginEncoded")!!
        end = intent.getStringExtra("DestinationEncoded")!!
        startName = intent.getStringExtra("OriginName")!!
        endName = intent.getStringExtra("DestinationName")!!

        // Set the text field of the TextViews
        findViewById<TextView>(R.id.origin).apply {
            text = startName
        }

        findViewById<TextView>(R.id.destination).apply {
            text = endName
        }

        // Hash map containing (travelMode, path) pairs for the three main paths
        mainPaths = mapOf(
            Constants.TRAVEL_MODE_DRIVING to createPath(startName, endName, "driving", null),
            Constants.TRAVEL_MODE_WALKING to createPath(startName, endName, "walking", null),
            Constants.TRAVEL_MODE_TRANSIT to createPath(startName, endName, "transit", null)
        )

        // Hash map containing (title, path) pairs for the optional transit paths
        extraPaths = mapOf(
            Constants.TITLE_RECOMMENDED_ROUTE to mainPaths.getValue("transit"),
            Constants.TITLE_LESS_WALKING to createPath(startName, endName, "transit", "less_walking"),
            Constants.TITLE_FEWER_TRANSFERS to createPath(startName, endName, "transit", "fewer_transfers")
        )

        // Display travel times
        GlobalScope.launch {
            for ((travelMode, path) in mainPaths) {
                path.waitUntilCreated()
                runOnUiThread {
                    val radioButtonId = "radio_$travelMode"
                    val id = resources.getIdentifier(radioButtonId, "id", packageName)
                    findViewById<RadioButton>(id).apply {
                        text = "${path.getDuration() / 60} min"
                        buttonTintList = colorStateList
                    }
                }
            }
            runOnUiThread {
                findViewById<TextView>(R.id.route_duration).text = "${currentPath.getDuration() / 60} min"
                findViewById<TextView>(R.id.route_distance).text = "(${currentPath.getDistance()})"
                findViewById<Button>(R.id.startButton).isEnabled = true
                findViewById<Button>(R.id.steps).isEnabled = true
            }
        }

        currentPath = mainPaths.getValue("driving")

        initializer.setOnMapReadyListener {
            setPathOnMapAsync(currentPath)
        }

        // Set Listener for accessibility popup
        setAccessPopup()

        steps.setOnClickListener {
            val routePreviewData = currentPath.getRoutePreviewData()
            routePreviewData.setStart(startName)
            routePreviewData.setEnd(endName)
            routePreviewData.setDistance(currentPath.getDistance())
            routePreviewData.setDuration(currentPath.getDuration() / 60)
            val studentDataObjectAsAString = Gson().toJson(routePreviewData)
            val stepIntent = Intent(this, StepsActivity::class.java)
            stepIntent.putExtra("Steps", studentDataObjectAsAString)
            this.startActivity(stepIntent)
        }

        startButton.setOnClickListener {
            val routePreviewData = currentPath.getRoutePreviewData()
            routePreviewData.setStart(startName)
            routePreviewData.setEnd(endName)
            val studentDataObjectAsAString = Gson().toJson(routePreviewData)
            val routePreview = Intent(this, RoutePreviewActivity::class.java)
                routePreview.putExtra("RoutePreview", studentDataObjectAsAString)
            this.startActivity(routePreview)
        }

        adapter = TransitRouteAdapter(this)
    }

    /**
     * Called when the Back button is clicked.
     */
    fun finishActivity(view: View) {
        finish()
    }

    /**
     * Called when a Radio button is clicked.
     */
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                R.id.radio_driving ->
                    if (checked) {
                        onTravelModeClicked(Constants.TRAVEL_MODE_DRIVING, mainPaths)
                    }
                R.id.radio_walking ->
                    if (checked) {
                        onTravelModeClicked(Constants.TRAVEL_MODE_WALKING, mainPaths)
                    }
                R.id.radio_transit ->
                    if (checked) {
                        hideMap()
                        initializeListView()
                    }
            }
            route_duration.text = "${currentPath.getDuration() / 60} min"
        }
    }

    /**
     * Called when the accessibility menu is clicked.
     */
    private fun setAccessPopup() {
        val button = findViewById<ImageButton>(R.id.accessibility_directions_popup)
        button.setOnClickListener {
            val popup: PopupMenu = PopupMenu(this, button)
            popup.menuInflater.inflate(R.menu.accessibility_menu, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.access_popup ->
                        handleAccessibilitySelect()
                }
                true
            })
            popup.show()
        }
    }

    private fun handleAccessibilitySelect() {
        val selectAccessibility = SelectAccessibilityOptionsDialogFragment(this)
        selectAccessibility.show(this.supportFragmentManager, "accessibilityOptions")
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onTravelModeClicked(adapter.getItem(position).title, extraPaths)
        // Change the transit travel time depending on which optional route was selected
        val radioButtonId = resources.getIdentifier("radio_transit", "id", packageName)
        findViewById<RadioButton>(radioButtonId).apply {
            text = "${adapter.getItem(position).duration / 60} min"
        }
        route_duration.text = "${currentPath.getDuration() / 60} min"
        route_distance.text = "(${currentPath.getDistance()})"
    }

    private fun isIndoorLocation(encodedLocation: String): Boolean {
        return encodedLocation.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)
    }

    private fun createSegment(location: String, args: SegmentArgs): Segment {
        return if (isIndoorLocation(location))
            IndoorSegment(location, args)
        else OutdoorSegment(location, args)
    }

    private fun setPathOnMapAsync(path: PathPolyline) {
        GlobalScope.launch {
            path.waitUntilCreated()
            runOnUiThread {
                map.addPath(path)
            }
        }
    }

    private fun createPath(startName: String, endName: String, travelMode: String, transitPreference: String?): PathPolyline {
        val errorListener = DisplayMessageErrorListener(this)
        val directions = OutdoorDirections(
            ApiKeyRequestDecorator(
                this,
                VolleyRequestDispatcher(
                    this,
                    errorListener
                )
            ),
            KlaxonDirectionsAPIResponseParser(),
            errorListener
        )
        val segmentArgs =
                SegmentArgs(travelMode, BuildingIndexSingleton.getInstance(assets), directions, transitPreference)

        val firstSegment = createSegment(start, segmentArgs)
        val secondSegment = createSegment(end, segmentArgs)
        secondSegment.appendTo(firstSegment)

        return PathPolyline(startName, endName, firstSegment)
    }

    private fun removePreviousPath() {
        if (::currentPath.isInitialized) {
            currentPath.removeFromMap()
        }
    }

    private fun showMap() {
        val mapFragment = this.supportFragmentManager.findFragmentById(R.id.directions_activity_map) as SupportMapFragment
        if (!mapFragment.isVisible) {
            this.supportFragmentManager.beginTransaction().show(mapFragment).commit()
            route_layout.visibility = View.VISIBLE
        }
    }

    private fun hideMap() {
        val mapFragment = this.supportFragmentManager.findFragmentById(R.id.directions_activity_map) as SupportMapFragment
        this.supportFragmentManager.beginTransaction().hide(mapFragment).commit()
        route_layout.visibility = View.GONE
    }

    private fun initializeListView() {
        if (!::listView.isInitialized) {
            listView = ListView(this)
            activity_directions_layout.addView(listView)
            listView.adapter = adapter
            listView.onItemClickListener = this
            for ((title, path) in extraPaths) {
                adapter.add(
                    TransitRoute(
                        title,
                        path.getSteps(),
                        path.getDuration(),
                        path.getFare()
                    )
                )
            }
            runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }

    private fun onTravelModeClicked(travelMode: String, paths: Map<String, PathPolyline>) {
        removePreviousPath()
        currentPath = paths.getValue(travelMode)
        setPathOnMapAsync(currentPath)
        showMap()
    }
}
