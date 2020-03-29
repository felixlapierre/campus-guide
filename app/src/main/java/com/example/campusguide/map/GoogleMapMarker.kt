package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.InfoWindowData

class GoogleMapMarker(private val marker: com.google.android.gms.maps.model.Marker) : Marker {
    override fun remove() {
        marker.remove()
    }

    override fun showInfoWindow() {
        marker.showInfoWindow()
    }

    override fun setTag(info: InfoWindowData) {
        marker.tag = info
    }

}