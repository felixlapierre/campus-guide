package com.example.campusguide.search.indoor

import com.example.campusguide.Constants
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationProvider
import java.lang.RuntimeException

/**
 * Provides the information of a location that was searched, including latitude
 * and longitude. Implements the chain of responsibility pattern in order to be
 * chained with other location providers.
 */
class IndoorLocationProvider constructor(
    private val index: BuildingIndex,
    private val next: SearchLocationProvider?
) : SearchLocationProvider {
    override suspend fun getLocation(id: String): SearchLocation? {
        val isIndoor = id.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)

        if(isIndoor) {
            val locationInfo = id.split("_")
            if(locationInfo.size != 3) {
                throw IdFormatException("Id $id is an indoor identifier that does not have the format indoor_buildingcode_roomcode")
            }
            val buildingCode = locationInfo[1]
            val roomCode = locationInfo[2]
            return getIndoorLocation(buildingCode, roomCode, id)
        } else {
            return next?.getLocation(id)
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
            ?: throw IndexNotLoadedException("Building index is not loaded yet")

        val targetBuilding = buildings?.find { building -> building.code == buildingCode }
            ?: throw BuildingNotFoundException("Building code $buildingCode does not correspond to a building in the index")

        val targetRoom = targetBuilding.rooms.find { room -> room.code == roomCode }
            ?: throw RoomNotFoundException("Room code $roomCode was not found in the index.")

        return SearchLocation(
            targetRoom.name,
            targetBuilding.name,
            id,
            targetRoom.lat.toDouble(),
            targetRoom.lon.toDouble()
        )
    }
}

class BuildingNotFoundException constructor(message: String) : RuntimeException(message)

class RoomNotFoundException constructor(message: String) : RuntimeException(message)

class IndexNotLoadedException constructor(message: String) : RuntimeException(message)

class IdFormatException constructor(message: String) : RuntimeException(message)