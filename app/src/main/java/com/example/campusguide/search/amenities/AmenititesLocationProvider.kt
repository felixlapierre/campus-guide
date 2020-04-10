package com.example.campusguide.search.amenities

import com.example.campusguide.Constants
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationProvider

class AmenititesLocationProvider constructor(
    private val next: SearchLocationProvider?
) : SearchLocationProvider {
    override suspend fun getLocation(id: String): SearchLocation? {
        val isAmenities = id.startsWith(Constants.AMENITIES_LOCATION_IDENTIFIER)

        if(isAmenities) {
            return getIndoorLocation(buildingCode, roomCode, id)
        } else {
            return next?.getLocation(id)
        }
    }
}