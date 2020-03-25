package com.example.campusguide.search.indoor

import com.google.android.gms.maps.model.LatLng

/**
 * Represents a class that can provide the buildings that are indexed
 * for indoor room search
 */
interface BuildingIndex {
    fun getBuildings(): List<Building>?
    fun getBuildingAtCoordinates(coordinates: LatLng): Building?
}