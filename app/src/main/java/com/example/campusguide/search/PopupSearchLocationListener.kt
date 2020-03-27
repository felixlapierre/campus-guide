package com.example.campusguide.search

import androidx.appcompat.app.AppCompatActivity

class PopupSearchLocationListener constructor(private val activity: AppCompatActivity): SearchLocationListener {
    override fun onLocation(location: SearchLocation) {
        activity.runOnUiThread {
            CurrentTravelPopup(activity, location)
        }
    }
}