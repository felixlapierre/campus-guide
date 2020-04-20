package com.example.campusguide

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
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
import com.example.campusguide.directions.LocationMetadata
import com.example.campusguide.directions.PathPolyline
import com.example.campusguide.directions.Route
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
import com.example.campusguide.map.displayIndoor.ChangeFloor
import com.example.campusguide.map.displayIndoor.FloorPlans
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.request.ApiKeyRequestDecorator
import com.example.campusguide.utils.request.VolleyRequestDispatcher
import com.google.android.gms.maps.SupportMapFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_directions.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class DirectionsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var map: GoogleMapAdapter
    private lateinit var start: LocationMetadata
    private lateinit var end: LocationMetadata
    private lateinit var currentPath: Route
    private lateinit var mainPaths: Map<String, Route>
    private lateinit var extraPaths: Map<String, Route>
    private var currentFloor: Int = 0
    private val floorPlans = FloorPlans()
    private val colorStateList: ColorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ), intArrayOf(
            Color.parseColor(Constants.PRIMARY_COLOR_DARK), // disabled
            Color.WHITE // enabled
        )
    )

    private lateinit var errorListener: DisplayMessageErrorListener
    private lateinit var buildingIndexSingleton: BuildingIndexSingleton

    private lateinit var listView: ListView
    private lateinit var adapter: TransitRouteAdapter

    private val giveMeAnOutdoorDirections =  {
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
        directions
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directions)

        this.errorListener= DisplayMessageErrorListener(this)
        this.buildingIndexSingleton = BuildingIndexSingleton.getInstance(assets)

        map = GoogleMapAdapter()
        // TODO: Refactor GoogleMapInitializer so it has less nullable constructor properties
        val initializer = GoogleMapInitializer(
            this,
            map,
            "directions_activity_map",
            null,
            null,
            null,
            BuildingIndexSingleton.getInstance(assets),
            floorPlans
        )
        setFloorPlanButtons()

        floorPlans.changeFloorListener = { floor ->
            onFloorChange(floor)
        }

        // Extract origin and destination from the intent
        start = LocationMetadata(
            encoded = intent.getStringExtra("OriginEncoded")!!,
            name = intent.getStringExtra("OriginName")!!,
            buildingIndexSingleton = buildingIndexSingleton
        )
        end = LocationMetadata(
            encoded = intent.getStringExtra("DestinationEncoded")!!,
            name = intent.getStringExtra("DestinationName")!!,
            buildingIndexSingleton = buildingIndexSingleton
        )

        // Set the text field of the TextViews
        findViewById<TextView>(R.id.origin).apply {
            text = start.name
        }

        findViewById<TextView>(R.id.destination).apply {
            text = end.name
        }

        createAllPaths()

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
                findViewById<TextView>(R.id.route_duration).text =
                    "${currentPath.getDuration() / 60} min"
                findViewById<TextView>(R.id.route_distance).text = "(${currentPath.getDistance()})"
                findViewById<Button>(R.id.startButton).isEnabled = true
                findViewById<Button>(R.id.steps).isEnabled = true
            }
        }

        currentPath = mainPaths.getValue("driving")

        initializer.setOnMapReadyListener {
            setPathsOnMapAsync(currentPath.getPathPolylines())
            centerMapOnPath(currentPath)
        }

        // Set Listener for accessibility popup
        setAccessPopup()

        steps.setOnClickListener {
            val routePreviewData = currentPath.getRoutePreviewDataRisky()
            routePreviewData.setStart(start.name)
            routePreviewData.setEnd(end.name)
            routePreviewData.setDistance(currentPath.getDistance())
            routePreviewData.setDuration(currentPath.getDuration() / 60)
            val studentDataObjectAsAString = Gson().toJson(routePreviewData)
            val stepIntent = Intent(this, StepsActivity::class.java)
            stepIntent.putExtra("Steps", studentDataObjectAsAString)
            this.startActivity(stepIntent)
        }

        startButton.setOnClickListener {
            val routePreviewData = currentPath.getRoutePreviewDataRisky()
            routePreviewData.setStart(start.name)
            routePreviewData.setEnd(start.name)
            val studentDataObjectAsAString = Gson().toJson(routePreviewData)
            val routePreview = Intent(this, RoutePreviewActivity::class.java)
            routePreview.putExtra("RoutePreview", studentDataObjectAsAString)
            this.startActivity(routePreview)
        }

        adapter = TransitRouteAdapter(this)
    }

    private fun createAllPaths() {
        // Hash map containing (travelMode, path) pairs for the three main paths
        mainPaths = mapOf(
            Constants.TRAVEL_MODE_DRIVING to Route(start, end, "driving", null,
                buildingIndexSingleton=buildingIndexSingleton,
                giveMeAnOutdoorDirections = giveMeAnOutdoorDirections
            ),
            Constants.TRAVEL_MODE_WALKING to Route(start, end, "walking", null,
                buildingIndexSingleton=buildingIndexSingleton,
                giveMeAnOutdoorDirections = giveMeAnOutdoorDirections
            ),
            Constants.TRAVEL_MODE_TRANSIT to Route(start, end, "transit", null,
                buildingIndexSingleton=buildingIndexSingleton,
                giveMeAnOutdoorDirections = giveMeAnOutdoorDirections
            ),
            Constants.TRAVEL_MODE_SHUTTLE to Route(start, end, "shuttle", null,
                buildingIndexSingleton=buildingIndexSingleton,
                giveMeAnOutdoorDirections = giveMeAnOutdoorDirections
            )
        )

        // Hash map containing (title, path) pairs for the optional transit paths
        extraPaths = mapOf(
            Constants.TITLE_RECOMMENDED_ROUTE to mainPaths.getValue("transit"),
            Constants.TITLE_LESS_WALKING to Route(start, end, "transit", "less_walking",
                buildingIndexSingleton=buildingIndexSingleton,
                giveMeAnOutdoorDirections = giveMeAnOutdoorDirections
            ),
            Constants.TITLE_FEWER_TRANSFERS to Route(start, end, "transit", "fewer_transfers",
                buildingIndexSingleton=buildingIndexSingleton,
                giveMeAnOutdoorDirections = giveMeAnOutdoorDirections
            )
        )
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
                        GlobalScope.launch {
                                initializeListView()
                        }
                    }
                R.id.radio_shuttle ->
                    if (checked) {
                        onTravelModeClicked(Constants.TRAVEL_MODE_SHUTTLE, mainPaths)
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
        val selectAccessibility = SelectAccessibilityOptionsDialogFragment(this) {
            createAllPaths()
            removePreviousPath()
            currentPath = mainPaths[Constants.TRAVEL_MODE_WALKING] ?: error("ERROR ")
            setPathsOnMapAsync(currentPath.getPathPolylines())
            centerMapOnPath(currentPath)
            showMap()
        }
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
                removePreviousPath()
                map.addPath(path, currentFloor)
            }
        }
    }
    private fun setPathsOnMapAsync(paths: List<PathPolyline>?) {
        if (paths.isNullOrEmpty()) {
            errorListener.onError("No route available")
            return
        }
        GlobalScope.launch {
            runOnUiThread { removePreviousPath() }
            paths.forEach {
                it.waitUntilCreated()
                runOnUiThread {
                    map.addPath(it, currentFloor)
                }
            }
        }
    }

    private fun centerMapOnPath(path: Route) {

            GlobalScope.launch {
                path.waitUntilCreated()
                runOnUiThread {
                    try {
                        map.moveCamera(path.getRouteBounds())
                    } catch (err: Exception) {

                    }
                }
            }


    }

    private fun removePreviousPath() {
        if (::currentPath.isInitialized) {
            currentPath.removeFromMap()
        }
    }

    private fun showMap() {
        val mapFragment =
            this.supportFragmentManager.findFragmentById(R.id.directions_activity_map) as SupportMapFragment
        if (!mapFragment.isVisible) {
            this.supportFragmentManager.beginTransaction().show(mapFragment).commit()
            route_layout.visibility = View.VISIBLE
            frame_layout.visibility = View.VISIBLE
        }
    }

    private fun hideMap() {
        val mapFragment =
            this.supportFragmentManager.findFragmentById(R.id.directions_activity_map) as SupportMapFragment
        this.supportFragmentManager.beginTransaction().hide(mapFragment).commit()
        route_layout.visibility = View.GONE
        frame_layout.visibility = View.GONE
    }

    private suspend fun initializeListView() {
        if (!::listView.isInitialized) {
            listView = ListView(this)
            runOnUiThread {
                activity_directions_layout.addView(listView)
            }
            listView.adapter = adapter
            listView.onItemClickListener = this
            for ((title, path) in extraPaths) {
                path.waitUntilCreated()
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

    private fun onTravelModeClicked(travelMode: String, paths: Map<String, Route>) {
        removePreviousPath()
        currentPath = paths.getValue(travelMode)
        setPathsOnMapAsync(currentPath.getPathPolylines())
        centerMapOnPath(currentPath)
        showMap()
    }

    private fun onFloorChange(floor: Int) {
        currentFloor = floor
        setPathsOnMapAsync(currentPath.getPathPolylines())
    }

    private fun setFloorPlanButtons() {
        ChangeFloor.upButtonId = R.id.upOneFloor
        ChangeFloor.downButtonId = R.id.downOneFloor
        floorPlans.floorUpButton = findViewById(R.id.upOneFloor)
        floorPlans.floorDownButton = findViewById(R.id.downOneFloor)
    }
}
