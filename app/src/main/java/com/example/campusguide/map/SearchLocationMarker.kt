package com.example.campusguide.map

import android.app.Activity
import com.example.campusguide.Constants
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class SearchLocationMarker constructor(private val activity: Activity, private val map: Map): SearchLocationListener {
    private var marker: Marker? = null

    override fun onLocation(location: SearchLocation) {
        activity.runOnUiThread {
            val coordinates = LatLng(location.lat, location.lon)
            marker?.remove()
            marker = map.addMarker(coordinates, location.name)
            map.animateCamera(coordinates, Constants.ZOOM_STREET_LVL)
        }
    }
}