package com.example.campusguide.location

import android.widget.CompoundButton
import android.widget.TextView
import com.example.campusguide.Constants
import com.example.campusguide.map.Map

class SwitchCampus constructor(private val map: Map, private val campusName: TextView) :
    CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        switchCampus(isChecked)
    }

    private fun switchCampus(isAtDowntownCampus: Boolean) {
        if (isAtDowntownCampus) {
            map.animateCamera(Constants.LOY_COORDINATES,
                Constants.ZOOM_STREET_LVL
            )
            campusName.text = Constants.LOYOLA_CAMPUS
        } else {
            map.animateCamera(Constants.SGW_COORDINATES,
                Constants.ZOOM_STREET_LVL
            )
            campusName.text = Constants.SGW_CAMPUS
        }
    }
}
