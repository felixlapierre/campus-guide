package com.example.campusguide.search.indoor

import com.example.campusguide.Constants
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationProvider

/**
 * Provides the information of a location that was searched, including latitude
 * and longitude. Implements the chain of responsibility pattern in order to be
 * chained with other location providers.
 */
class IndoorLocationProvider constructor(
    private val index: BuildingIndex,
    private val next: SearchLocationProvider
) : SearchLocationProvider {
    override suspend fun getLocation(id: String): SearchLocation {
        val locationInfo = id.split("_")
        val isIndoor = locationInfo[0] == Constants.INDOOR_LOCATION_IDENTIFIER
        val buildingCode = locationInfo[1]
        val roomCode = locationInfo[2]

        if (isIndoor) {
            return getIndoorLocation(buildingCode, roomCode, id)
        } else {
            return next.getLocation(id)
        }
    }

    private fun getIndoorLocation(
        buildingCode: String,
        roomCode: String,
        id: String
    ): SearchLocation {
        /**
         * Getting the buildings from the index will return null if the index hasn't been loaded
         * yet. This shouldn't happen because the index will have been loaded during the search.
         */
        val buildings = index.getBuildings()
        val targetBuilding = buildings?.find { building -> building.code == buildingCode }
        if (targetBuilding != null) {
            val targetRoom = targetBuilding.rooms.find { room -> room.code == roomCode }
            if (targetRoom != null) {
                return SearchLocation(
                    targetRoom.name,
                    id,
                    targetRoom.lat.toDouble(),
                    targetRoom.lon.toDouble()
                )
            }
        }

        throw Exception("Could not find indoor room $id")
    }
}