package com.example.campusguide

import android.widget.TextView
import android.widget.ToggleButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class SwitchCampus constructor(toggleBtn: ToggleButton, private val map: Map, campusName: TextView) {

    init {
        toggleBtn.setOnCheckedChangeListener{ _, isAtDowntownCampus ->
            switchCampus(isAtDowntownCampus, campusName)
        }
    }

    private fun switchCampus(isAtDowntownCampus: Boolean, campusName: TextView) {
        if(isAtDowntownCampus){
            val loyCoord = LatLng(45.458153, -73.640490)
            map.animateCamera(loyCoord, Constants.ZOOM_STREET_LVL)
            campusName.text = Constants.LOYOLA_CAMPUS
        } else {
            val sgwCoord = LatLng(45.495792, -73.578096)
            map.animateCamera(sgwCoord, Constants.ZOOM_STREET_LVL)
            campusName.text = Constants.SGW_CAMPUS
        }
    }
}