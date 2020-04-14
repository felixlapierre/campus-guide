package com.example.campusguide.map.displayIndoor

import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng

class BuildingInfo constructor(buildingName: String, map: GoogleMapAdapter) {

    private val floors: IntArray? = setFloors(buildingName)
        fun getFloors(): IntArray? {
            return floors
        }
    private val buildingImageCoordinates: LatLng = setBuildingImageCoordinates(buildingName)
    fun getBuildingImageCoordinates(): LatLng {
        return buildingImageCoordinates
    }
    private val floorPlans: HashMap<Int, GroundOverlay>? = setUpFloorPlans(buildingName, map)
        fun getFloorPlans(): HashMap<Int, GroundOverlay>? {
            return floorPlans
        }

    val startFloor: Int? = floors?.get(0)

    private fun setBuildingImageCoordinates(buildingName: String): LatLng {
        if (buildingName == "hall")     return LatLng(45.4972695, -73.57894175)
        if (buildingName == "library")  return LatLng(45.496753, -73.577904)

        return LatLng(0.0, 0.0)
    }
    private fun setFloors(buildingName: String): IntArray? {
        if (buildingName == "hall")     return intArrayOf(4, 5, 6, 7, 8)
        if (buildingName == "library")  return intArrayOf(2, 3, 4, 5)

        return null
    }

    private fun setUpFloorPlans(buildingName: String, map: GoogleMapAdapter): HashMap<Int, GroundOverlay>? {

        if (floors == null)
        {
            return null
        }

        val buildingFloors = hashMapOf<Int, GroundOverlay>()

        if (buildingName == "hall") {

            for (floor in floors) {
                buildingFloors[floor] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromAsset("h_floor$floor.png"))
                        .position(buildingImageCoordinates, 68F, 63F).bearing(124F)
                        .visible(false)
                        .zIndex(3F)
                )
            }
        } else if (buildingName == "library") {
            for (floor in floors) {
                buildingFloors[floor] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromAsset("lb_floor$floor.png"))
                        .position(buildingImageCoordinates, 82F, 82F).bearing(-56F)
                        .visible(false)
                        .zIndex(3F)
                )
            }
        }

        return buildingFloors
    }
}
