package com.example.campusguide

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.directions.DOWNTOWN_CAMPUS_BOUNDS
import com.example.campusguide.directions.KlaxonDirectionsAPIResponseParser
import com.example.campusguide.directions.LOYOLA_CAMPUS_BOUNDS
import com.example.campusguide.directions.LocationMetadata
import com.example.campusguide.directions.Route
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.request.ApiKeyRequestDecorator
import com.example.campusguide.utils.request.VolleyRequestDispatcher
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DirectionsActivity : AppCompatActivity() {
    private lateinit var map: GoogleMapAdapter
    private lateinit var start: LocationMetadata
    private lateinit var end: LocationMetadata
    private lateinit var currentRoute: Route
    private lateinit var paths: Map<String, Route>
    private lateinit var errorListener: DisplayMessageErrorListener
    private lateinit var buildingIndexSingleton: BuildingIndexSingleton
    private val colorStateList: ColorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ), intArrayOf(
            Color.BLACK, // disabled
            Color.parseColor(Constants.PRIMARY_COLOR_DARK) // enabled
        )
    )
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
        val initializer = GoogleMapInitializer(this, map, "directions_activity_map")

        // Extract origin and destination from the intent
        start = LocationMetadata(
            encoded = intent.getStringExtra("OriginEncoded")!!,
            name = intent.getStringExtra("OriginName")!!
        )
        end = LocationMetadata(
            encoded = intent.getStringExtra("DestinationEncoded")!!,
            name = intent.getStringExtra("DestinationName")!!
        )
        println("Encoded value is: ${start.encoded}")

        // Set the text field of the TextViews
        findViewById<TextView>(R.id.origin).apply {
            text = start.name
        }

        findViewById<TextView>(R.id.destination).apply {
            text = end.name
        }

        // Hash map containing (travelMode, path) pairs
        paths = mapOf(
            "driving" to Route(start, end, "driving",
                buildingIndexSingleton, giveMeAnOutdoorDirections),
            "walking" to Route(start, end, "walking",
                buildingIndexSingleton, giveMeAnOutdoorDirections),
            "transit" to Route(start, end, "transit",
                buildingIndexSingleton, giveMeAnOutdoorDirections),
            "shuttle" to Route(start, end, "shuttle",
                buildingIndexSingleton, giveMeAnOutdoorDirections)
        )

        // Display travel times
        GlobalScope.launch {
            for ((travelMode, path) in paths) {
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
        }

        currentRoute = paths.getValue("driving")

        initializer.setOnMapReadyListener {
            setRouteOnMapAsync(currentRoute)
        }
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
                        removePreviousPath()
                        currentRoute = paths.getValue("driving")
                        setRouteOnMapAsync(currentRoute)
                    }
                R.id.radio_walking ->
                    if (checked) {
                        removePreviousPath()
                        currentRoute = paths.getValue("walking")
                        setRouteOnMapAsync(currentRoute)
                    }
                R.id.radio_transit ->
                    if (checked) {
                        removePreviousPath()
                        currentRoute = paths.getValue("transit")
                        setRouteOnMapAsync(currentRoute)
                    }
                R.id.radio_shuttle ->
                    if (checked) {
                        removePreviousPath()
                        currentRoute = paths.getValue("shuttle")
                        setRouteOnMapAsync(currentRoute)
                    }
            }
        }
    }

    private fun setRouteOnMapAsync(route: Route) {
        GlobalScope.launch {
            route.waitUntilCreated()
            runOnUiThread {
               val  pathPolylines = route.getPathPolylines()
                if (pathPolylines.isNullOrEmpty()) {
                    errorListener.onError("No shuttle bus route available.")
                }else{
                    pathPolylines.forEach { map.addPath(it) }
                    map.moveCamera(pathPolylines.first().getPathBounds()
                        // .including(pathPolylines.last().getPathBounds().northeast)
                        // .including(pathPolylines.last().getPathBounds().southwest)
                    )
                }

            }
        }
    }

    private fun removePreviousPath() {
        if (::currentRoute.isInitialized) {
            currentRoute.removeFromMap()
        }
    }
}
