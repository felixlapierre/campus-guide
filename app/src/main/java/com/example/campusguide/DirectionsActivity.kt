package com.example.campusguide

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.directions.*

import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.request.ApiKeyRequestDecorator
import com.example.campusguide.utils.request.VolleyRequestDispatcher
import com.google.android.gms.maps.model.Polyline
import com.google.maps.model.TravelMode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DirectionsActivity : AppCompatActivity() {

    private lateinit var map: GoogleMapAdapter
    private lateinit var start: String
    private lateinit var end: String
    private lateinit var startName: String
    private lateinit var endName: String
    private var travelMode = "Driving"
    private lateinit var path: PathPolyline

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
        path = createPath(startName, endName, travelMode)
        initializer.setOnMapReadyListener {
            setPathOnMapAsync(path)
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
                        travelMode = "Driving"
                        path = createPath(startName, endName, travelMode)
                        setPathOnMapAsync(path)
                    }
                R.id.radio_walking ->
                    if (checked) {
                        travelMode = "Walking"
                        path = createPath(startName, endName, travelMode)
                        setPathOnMapAsync(path)
                    }
                R.id.radio_transit ->
                    if (checked) {
                        travelMode = "Transit"
                        path = createPath(startName, endName, travelMode)
                        setPathOnMapAsync(path)
                    }
            }
        }
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
                val radioButtonId = "radio_" + travelMode.toLowerCase()
                val id = resources.getIdentifier(radioButtonId, "id", packageName)
                findViewById<RadioButton>(id).apply {
                    text = "${path.segment.getDuration() / 60} min"
                }
            }
        }
    }

    private fun createPath(startName: String, endName: String, travelMode: String): PathPolyline {
        if (::path.isInitialized) {
            path.removeFromMap()
        }
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
            SegmentArgs(travelMode, BuildingIndexSingleton.getInstance(assets), directions)

        val firstSegment = createSegment(start, segmentArgs)
        val secondSegment = createSegment(end, segmentArgs)
        secondSegment.appendTo(firstSegment)

        return PathPolyline(startName, endName, firstSegment)
    }
}
