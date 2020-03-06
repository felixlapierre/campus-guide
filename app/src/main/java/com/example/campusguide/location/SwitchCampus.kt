package com.example.campusguide.location

import android.widget.CompoundButton
import android.widget.TextView
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.google.android.gms.maps.model.LatLng

class SwitchCampus constructor(private val map: Map, private val campusName: TextView)
    : CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        switchCampus(isChecked)
    }

    private fun switchCampus(isAtDowntownCampus: Boolean) {
        if(isAtDowntownCampus){
            val loyCoord = LatLng(45.458153, -73.640490)
            map.animateCamera(loyCoord,
                Constants.ZOOM_STREET_LVL
            )
            campusName.text = Constants.LOYOLA_CAMPUS
        } else {
            val sgwCoord = LatLng(45.495792, -73.578096)
            map.animateCamera(sgwCoord,
                Constants.ZOOM_STREET_LVL
            )
            campusName.text = Constants.SGW_CAMPUS
        }
    }
}