package com.example.campusguide.map.displayIndoor

import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.Marker

class Floor(
    val overlay: GroundOverlay,
    val amenities: List<Marker>
){
    public fun displayFloor(){
        setVisible(true)
    }

    public fun hideFloor(){
        setVisible(false)
    }

    public fun setVisible(isVisible: Boolean){
        overlay.isVisible = isVisible
        for(marker in amenities){
            marker.isVisible = isVisible
        }
    }
}
