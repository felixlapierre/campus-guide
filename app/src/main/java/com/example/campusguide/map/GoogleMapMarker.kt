package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.MarkerTag

class GoogleMapMarker(private val marker: com.google.android.gms.maps.model.Marker) : IMarker {
    override fun remove() {
        marker.remove()
    }

    override fun showInfoWindow() {
        marker.showInfoWindow()
    }

    override fun setTag(info: MarkerTag) {
        marker.tag = info
    }
}
