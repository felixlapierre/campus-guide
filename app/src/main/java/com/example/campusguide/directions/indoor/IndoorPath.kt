package com.example.campusguide.directions.indoor

import com.example.campusguide.directions.Path
import com.google.android.gms.maps.model.LatLng

class IndoorPath(points: MutableList<LatLng>, val floor: Int) : Path(points) {
    override fun shouldDisplay(currentFloor: Int) : Boolean {
        return currentFloor == floor
    }
}