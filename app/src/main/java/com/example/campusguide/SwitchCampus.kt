package com.example.campusguide

import android.widget.TextView
import android.widget.ToggleButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class SwitchCampus {

    private lateinit var mMap : GoogleMap

    constructor(
        toggleBtn: ToggleButton,
        mMap: GoogleMap,
        campusName: TextView
    ) {
        this.mMap = mMap
        toggleBtn.setOnCheckedChangeListener{ _, isAtDowntownCampus ->
            switchCampus(isAtDowntownCampus, campusName)
        }
    }

    private fun switchCampus(isAtDowntownCampus: Boolean, campusName: TextView) {
        if(isAtDowntownCampus){
            val loyCoord = LatLng(45.458153, -73.640490)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loyCoord, Constants.ZOOM_STREET_LVL))
            campusName.text = "Loyola Campus"
        } else {
            val sgwCoord = LatLng(45.495792, -73.578096)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sgwCoord, Constants.ZOOM_STREET_LVL))
            campusName.text = "SGW Campus"
        }
    }

}