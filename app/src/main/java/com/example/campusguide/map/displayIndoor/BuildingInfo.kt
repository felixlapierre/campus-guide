package com.example.campusguide.map.displayIndoor

import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng

class BuildingInfo constructor(private val buildingName: String, map: GoogleMapAdapter) {
    private val floors: IntArray? = setFloors()
    private val buildingImageCoordinates: LatLng = setBuildingImageCoordinates()
    private val floorPlans: HashMap<Int, GroundOverlay>? = setUpFloorPlans(map)
    val startFloor: Int? = floors?.get(0)

    fun getFloors(): IntArray? {
        return floors
    }
    fun getBuildingImageCoordinates(): LatLng {
        return buildingImageCoordinates
    }
    fun getFloorPlans(): HashMap<Int, GroundOverlay>? {
        return floorPlans
    }

    private fun setBuildingImageCoordinates(): LatLng {
        if (buildingName == "hall") return LatLng(45.4972695, -73.57894175)
        if (buildingName == "library") return LatLng(45.496753, -73.577904)

        return LatLng(0.0, 0.0)
    }
    private fun setFloors(): IntArray? {
        if (buildingName == "hall") return intArrayOf(4, 5, 6, 7, 8)
        if (buildingName == "library") return intArrayOf(2, 3, 4, 5)

        return null
    }
    private fun setUpFloorPlans(map: GoogleMapAdapter): HashMap<Int, GroundOverlay>? {

        if (floors != null) {
            if (buildingName == "hall") {
                return createGroundOverlays("h", map, 68F, 63F, 124F)
            } else if (buildingName == "library") {
                return createGroundOverlays("lb", map, 82F, 82F, -56F)
            }
        }
        return null
    }

    private fun createGroundOverlays(buildingCode: String, map: GoogleMapAdapter, length: Float, width: Float, bearing: Float): HashMap<Int, GroundOverlay> {
        val buildingFloors = hashMapOf<Int, GroundOverlay>()

        if (floors != null) {
            for (floor in floors) {
                buildingFloors[floor] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromAsset("${buildingCode}_floor$floor.png"))
                        .position(buildingImageCoordinates, length, width).bearing(bearing)
                        .visible(false)
                        .zIndex(3F)
                )
            }
        }
        return buildingFloors
    }
}
