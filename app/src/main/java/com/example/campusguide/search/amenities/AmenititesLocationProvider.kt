package com.example.campusguide.search.amenities

import com.example.campusguide.Constants
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationProvider

class AmenitiesLocationProvider constructor(
    private val next: SearchLocationProvider?
) : SearchLocationProvider {
    override suspend fun getLocation(id: String): SearchLocation? {
        val isAmenities = id.startsWith(Constants.AMENITIES_LOCATION_IDENTIFIER)
        println("-------------------------id: " + id)
        println("-------------------------is amenities: " + isAmenities)

        return if(isAmenities) {
            println("-------------------------WOW amenities!")
            getAmenities()
        } else {
            next?.getLocation(id)
        }
    }

    private fun getAmenities(): SearchLocation {
        return SearchLocation("name", 11.11, 22.22, "amenities_bathroom", "secondary text")
    }
}