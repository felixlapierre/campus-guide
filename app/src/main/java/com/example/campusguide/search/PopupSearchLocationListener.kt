package com.example.campusguide.search

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.directions.DirectionsFlow

class PopupSearchLocationListener constructor(private val activity: AppCompatActivity, private val directions: DirectionsFlow): SearchLocationListener {
    override fun onLocation(location: SearchLocation) {
        activity.runOnUiThread {
            CurrentTravelPopup(activity, location, directions)
        }
    }
}