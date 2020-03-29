package com.example.campusguide

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import android.widget.ToggleButton

import androidx.appcompat.app.AppCompatActivity

import com.example.campusguide.directions.Route
import com.example.campusguide.directions.ShuttleBusRouteManager
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.google.maps.model.TravelMode

class DirectionsActivity : AppCompatActivity() {

    private lateinit var map: GoogleMapAdapter
    private lateinit var route: Route
    private lateinit var start: String
    private lateinit var end: String
    private var travelMode: TravelMode = TravelMode.WALKING
    private var takingShuttle: Boolean = false // TODO: Dialog fragment for shuttle preference?
    private lateinit var shuttleBusRouteManager: ShuttleBusRouteManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directions)
        findViewById<ToggleButton>(R.id.switchCampusButton).visibility = View.INVISIBLE

        map = GoogleMapAdapter()
        GoogleMapInitializer(this, map, "directions_activity_map")

        // Extract origin and destination from the intent
        start = intent.getStringExtra("Origin")!!
        end = intent.getStringExtra("Destination")!!

        // Set the text field of the TextViews
        findViewById<TextView>(R.id.origin).apply {
            text = start
        }
        findViewById<TextView>(R.id.destination).apply {
            text = end
        }

        // Display driving directions by default as soon as the activity gets created
        route = Route(map, this)
        updateDirections(start, end, travelMode)

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
                        this.updateDirections(start, end, TravelMode.DRIVING)
                    }
                R.id.radio_walking ->
                    if(checked) {
                        this.updateDirections(start, end, TravelMode.WALKING)
                    }
                R.id.radio_transit ->
                    if(checked) {
                        this.updateDirections(start, end, TravelMode.TRANSIT)
                    }
            }
        }
    }

    private fun updateDirections(start: String, end: String, travelMode: TravelMode) {
        this.travelMode = travelMode
        route.set(start, end, travelMode)


        if (takingShuttle) {
            findViewById<ToggleButton>(R.id.switchCampusButton).visibility = View.VISIBLE;
        }
    }
    private fun routeSetCallback(): Unit {

    }
    fun setSwitchCampusButtonListener(listener: CompoundButton.OnCheckedChangeListener) {
        val toggleButton: ToggleButton = findViewById(R.id.switchCampusButton)
        toggleButton.setOnCheckedChangeListener(listener)
    }
}
