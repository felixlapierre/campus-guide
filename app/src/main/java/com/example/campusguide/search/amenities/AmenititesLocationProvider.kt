package com.example.campusguide.search.amenities

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.Constants
import com.example.campusguide.directions.ChooseOriginOptions
import com.example.campusguide.directions.indoor.Graph
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.Location
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationProvider
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.permissions.Permissions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AmenitiesLocationProvider constructor(
    private val next: SearchLocationProvider?,
    private val permissions: Permissions,
    private val activity: AppCompatActivity,
    private val locationProvider: FusedLocationProvider
) : SearchLocationProvider {
    override suspend fun getLocation(id: String): SearchLocation? {
        val isAmenities = id.startsWith(Constants.AMENITIES_LOCATION_IDENTIFIER)
        val lbBuildingIndex =  BuildingIndexSingleton.getInstance(activity.assets).findBuildingByCode("LB")!!
        println("-------------------------id: " + id)
        println("-------------------------is amenities: " + isAmenities)

        if(isAmenities) {
            println("-------------------------WOW amenities!")
            val origin = getOrigin().encodeForDirections()
            var start = ""
            if (origin.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)) {
                start = origin.split("_")[2]
            } else {
                start = lbBuildingIndex.nodes[0].code
            }
            val listOfBathrooms = AmenitiesPathfinding(
                Graph(
                   lbBuildingIndex
                )).findRoom(start)
            return SearchLocation("Bathroom", listOfBathrooms[0][listOfBathrooms[0].size - 1].latitude, listOfBathrooms[0][listOfBathrooms[0].size - 1].longitude, "amenities_bathroom", "")
        } else {
            return next?.getLocation(id)
        }
    }

    private suspend fun getOrigin() = suspendCoroutine<Location> { cont ->
        val chooseOriginOptions = ChooseOriginOptions(permissions, locationProvider) { origin ->
            cont.resume(origin)
        }
        chooseOriginOptions.show(activity.supportFragmentManager, "chooseOriginOptions")
    }
}