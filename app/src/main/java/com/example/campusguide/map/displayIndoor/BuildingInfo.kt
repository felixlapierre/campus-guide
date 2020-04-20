package com.example.campusguide.map.displayIndoor

import android.app.Activity
import android.graphics.Color
import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.infoWindow.IndoorTag
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.Helper
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.floor

class BuildingInfo constructor(private val buildingName: String, private val map: GoogleMapAdapter, private val buildingIndexSingleton: BuildingIndexSingleton, private val directionsFlow: DirectionsFlow?, private val activity: Activity) {
    private val floors: IntArray? = setFloors()
    private val buildingImageCoordinates: LatLng = setBuildingImageCoordinates()
    private var floorPlans: HashMap<Int, Floor>? = null
    val startFloor: Int? = floors?.get(0)

    init {
        setupFloorPlansFromBuildingIndex()
    }
    fun getFloors(): IntArray? {
        return floors
    }
    fun getBuildingImageCoordinates(): LatLng {
        return buildingImageCoordinates
    }
    fun getFloorPlans(): HashMap<Int, Floor>? {
        if (floorPlans == null) {
            setupFloorPlansFromBuildingIndex()
        }
        return floorPlans
    }

    private fun setBuildingImageCoordinates(): LatLng {
        if (buildingName == Constants.HALL) return LatLng(45.4972695, -73.57894175)
        if (buildingName == Constants.LIBRARY) return LatLng(45.496753, -73.577904)

        return LatLng(0.0, 0.0)
    }
    private fun setFloors(): IntArray? {
        if (buildingName == Constants.HALL) return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        if (buildingName == Constants.LIBRARY) return intArrayOf(1, 2, 3, 4, 5)

        return null
    }

    private fun setupFloorPlansFromBuildingIndex() {
        var buildings = buildingIndexSingleton.getBuildings()
        if (buildings == null) {
            buildingIndexSingleton.onLoaded = { it ->
                floorPlans = setUpFloorPlans(map, it)
            }
        } else {
            floorPlans = setUpFloorPlans(map, buildings)
        }
    }
    private fun setUpFloorPlans(map: GoogleMapAdapter, buildings: List<Building>): HashMap<Int, Floor>? {

        if (floors != null) {
            if (buildingName == Constants.HALL) {
                return createGroundOverlays(Constants.HALL_CODE, map, 68F, 63F, 124F, buildings)
            } else if (buildingName == Constants.LIBRARY) {
                return createGroundOverlays(Constants.LIBRARY_CODE, map, 82F, 82F, -56F, buildings)
            }
        }
        return null
    }


    private fun createGroundOverlays(buildingCode: String, map: GoogleMapAdapter, length: Float, width: Float, bearing: Float, buildings: List<Building>): HashMap<Int, Floor> {
        val buildingFloors = hashMapOf<Int, Floor>()
        val building = buildings!!.first { it.code?.equals(buildingCode, true) }
        if (floors != null) {
            for (floor in floors) {
                activity.runOnUiThread {
                    val overlay = map.adapted.addGroundOverlay(
                        GroundOverlayOptions()
                            .image(BitmapDescriptorFactory.fromAsset("${buildingCode}_floor$floor.png"))
                            .position(buildingImageCoordinates, length, width).bearing(bearing)
                            .visible(false)
                            .zIndex(3F)
                    )
                    val amenities = getFloorAmenities(building, floor, map)
                    buildingFloors[floor] = Floor(overlay, amenities)
                }
            }
        }

        return buildingFloors
    }

    /**
     * Finds the amenities on a building's floor and return a list of map markers
     * @param building The building in which to find the rooms
     * @param floorNumber The floornumber on which we want to find amenities
     * @param map The map to which the markers are added
     * @return A list of map markers
     */
    private fun getFloorAmenities(building: Building, floorNumber: Int, map: GoogleMapAdapter): List<Marker> {
        if (directionsFlow == null)
            return emptyList()
        var amenities: MutableList<Marker> = mutableListOf()
        for (room in building.rooms) {
            val imageDescription =
                BitmapDescriptorFactory.fromBitmap(Helper.textAsBitmap(room.code, 40f, Color.BLACK))
            if (room.code.toDouble().toInt() in (floorNumber * 100)..((floorNumber + 1) * 100)) {
                val marker = map.adapted.addMarker(
                    MarkerOptions()
                        .position(LatLng(room.lat.toDouble(), room.lon.toDouble()))
                        .title(room.code)
                        .icon(imageDescription)
                        .visible(false)
                )
                val roomId = "indoor_${building.code}_${room.code}"
                marker.tag = IndoorTag(R.layout.confirm_window, directionsFlow, room, roomId)
                amenities.add(marker)
            }
        }
        return amenities
    }
}
