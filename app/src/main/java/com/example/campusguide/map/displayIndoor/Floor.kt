package com.example.campusguide.map.displayIndoor

import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.Marker

class Floor(
    val overlay: GroundOverlay,
    val amenities: List<Marker>
) {
    fun displayFloor() {
        setVisible(true)
    }

    fun hideFloor() {
        setVisible(false)
    }

    fun setVisible(isVisible: Boolean) {
        overlay.isVisible = isVisible
        for (marker in amenities) {
            marker.isVisible = isVisible
        }
    }
}
