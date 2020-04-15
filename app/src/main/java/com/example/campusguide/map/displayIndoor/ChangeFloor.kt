package com.example.campusguide.map.displayIndoor

import android.view.View
import com.example.campusguide.map.GoogleMapAdapter
import kotlin.collections.HashMap

class ChangeFloor constructor(private val map: GoogleMapAdapter) : View.OnClickListener {

    private val buildings = buildBuildings(map)
    private var currentBuilding: String = ""
    private var currentFloor = -1

    companion object ButtonInfo {
        var upButtonId = -1
        var downButtonId = -1
    }

    private fun buildBuildings(map: GoogleMapAdapter): HashMap<String, BuildingInfo> {
        val build = HashMap<String, BuildingInfo>()
        build["hall"] = BuildingInfo("hall", map)
        build["library"] = BuildingInfo("library", map)
        return build
    }

    fun setBuilding(buildingName: String) {

        if (buildingName != currentBuilding) {
            unsetBuilding()
        }
        currentBuilding = buildingName
        currentFloor = buildings[buildingName]?.startFloor!!
        currentFloorIsVisible(true)
    }

    fun unsetBuilding() {
        currentFloorIsVisible(false)
        currentBuilding = ""
        currentFloor = -1
    }

    override fun onClick(v: View?) {

        val buildingImageLatLng = buildings[currentBuilding]?.getBuildingImageCoordinates()
        var updatedFloor: Int = 0
        if (v?.id == upButtonId) {
            updatedFloor = updateFloorUp(currentFloor)
        } else if (v?.id == downButtonId) {
            updatedFloor = updateFloorDown(currentFloor)
        }
        buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = false
        buildings[currentBuilding]?.getFloorPlans()?.get(updatedFloor)?.isVisible = true

        currentFloor = updatedFloor

        if (buildingImageLatLng != null) {
            map.animateCamera(buildingImageLatLng, map.adapted.cameraPosition.zoom)
        }
    }

    private fun currentFloorIsVisible(isVisible: Boolean) {
        if (currentBuilding != "") {
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = isVisible
        }
    }
    private fun updateFloorUp(currentFloor: Int): Int {

        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if (currentFloor >= floorNumbers!![floorNumbers.size - 1]) {
            return floorNumbers!![floorNumbers.size - 1]
        }
        return currentFloor + 1
    }

    private fun updateFloorDown(currentFloor: Int): Int {
        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if (currentFloor <= floorNumbers!![0]) {
            return floorNumbers!![0]
        }
        return (currentFloor - 1)
    }
}
