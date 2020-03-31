package com.example.campusguide

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.directions.KlaxonDirectionsAPIResponseParser

import com.example.campusguide.directions.Route
import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.indoor.IndoorRoute
import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.indoor.Pathfinding
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.request.ApiKeyRequestDecorator
import com.example.campusguide.utils.request.VolleyRequestDispatcher

class DirectionsActivity : AppCompatActivity() {

    private lateinit var map: GoogleMapAdapter
    private lateinit var route: Route
    private lateinit var start: String
    private lateinit var end: String
    private var travelMode = "Driving"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directions)

        map = GoogleMapAdapter()
        GoogleMapInitializer(this, map, "directions_activity_map")

        // Extract origin and destination from the intent
        start = intent.getStringExtra("OriginEncoded")!!
        end = intent.getStringExtra("DestinationEncoded")!!
        val startName = intent.getStringExtra("OriginName")!!
        val endName = intent.getStringExtra("DestinationName")!!

        // Set the text field of the TextViews
        findViewById<TextView>(R.id.origin).apply {
            text = startName
        }
        findViewById<TextView>(R.id.destination).apply {
            text = endName
        }

        val errorListener = DisplayMessageErrorListener(this);
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

        firstSegment.display(map)

        // route = Route(map, this)
        // route.set(start, end, travelMode)
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
                        route.set(start, end, travelMode)
                    }
                R.id.radio_walking ->
                    if (checked) {
                        travelMode = "Walking"
                        route.set(start, end, travelMode)
                    }
                R.id.radio_transit ->
                    if (checked) {
                        travelMode = "Transit"
                        route.set(start, end, travelMode)
                    }
            }
        }
    }

    private fun isIndoorLocation(encodedLocation: String): Boolean {
        return encodedLocation.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)
    }

    private fun createSegment(location: String, args: SegmentArgs): Segment {
        return if (isIndoorLocation(location)) IndoorSegment(location, args) else OutdoorSegment(
            location,
            args
        )
    }
}
