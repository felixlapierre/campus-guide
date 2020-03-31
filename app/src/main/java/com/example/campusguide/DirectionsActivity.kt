package com.example.campusguide

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.example.campusguide.directions.Route
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer

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

        // Set the text field of the TextViews
        findViewById<TextView>(R.id.origin).apply {
            text = start
        }
        findViewById<TextView>(R.id.destination).apply {
            text = end
        }

        // Display driving directions by default as soon as the activity gets created
        route = Route(map, this)
        route.set(start, end, travelMode)

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
                    if(checked) {
                        travelMode = "Driving"
                        route.set(start, end, travelMode)
                    }
                R.id.radio_walking ->
                    if(checked) {
                        travelMode = "Walking"
                        route.set(start, end, travelMode)
                    }
                R.id.radio_transit ->
                    if(checked) {
                        travelMode = "Transit"
                        route.set(start, end, travelMode)
                    }
            }
        }
    }
}
