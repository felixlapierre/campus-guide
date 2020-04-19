package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLng

/**
 * Represents a path between two places.
 * This class exists so the directions can be split into sub-paths based on criteria that make them
 * display or not. Basically, this exists so we can display only the current floor of indoor directions.
 */
open class Path(val points: MutableList<LatLng>) {
    open fun shouldDisplay(currentFloor: Int): Boolean {
        return true
    }
}
