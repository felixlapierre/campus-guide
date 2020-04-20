package com.example.campusguide.map.displayIndoor

import android.app.Activity
import android.view.View
import com.example.campusguide.Constants
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import kotlin.collections.HashMap

class ChangeFloor constructor(
    private val map: GoogleMapAdapter,
    private val buildingIndexSingleton: BuildingIndexSingleton,
    private val directionsFlow: DirectionsFlow?,
    private val activity: Activity
) : View.OnClickListener {

    private val buildings = buildBuildings(map)
    private var currentBuilding: String = ""
    private var currentFloor = -1
    var changeFloorListener: ((Int) -> Unit)? = null

    companion object ButtonInfo {
        var upButtonId = -1
        var downButtonId = -1
    }

    private fun buildBuildings(map: GoogleMapAdapter): HashMap<String, BuildingInfo> {
        val build = HashMap<String, BuildingInfo>()
        build[Constants.HALL] = BuildingInfo(Constants.HALL, map, buildingIndexSingleton, directionsFlow, activity)
        build[Constants.LIBRARY] = BuildingInfo(Constants.LIBRARY, map, buildingIndexSingleton, directionsFlow, activity)
        return build
    }

    fun setBuilding(buildingName: String) {

        if (buildingName != currentBuilding) {
            unsetBuilding()
        }
        currentBuilding = buildingName
        currentFloor = buildings[buildingName]?.startFloor!!
        currentFloorIsVisible(true)
        changeFloorListener?.invoke(currentFloor)
    }

    fun unsetBuilding() {
        currentFloorIsVisible(false)
        currentBuilding = ""
        currentFloor = -1
    }

    override fun onClick(v: View?) {
        var updatedFloor: Int = updateFloor(v?.id)
        changeFloorListener?.invoke(updatedFloor)

        buildings[currentBuilding]?.getFloorPlans()?.let { changeVisibleFloor(it, updatedFloor) }
    }

    private fun currentFloorIsVisible(isVisible: Boolean) {
        if (currentBuilding != "") {
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.setVisible(isVisible)
        }
    }

    private fun updateFloor(buttonId: Int?): Int {
        return when (buttonId) {
            upButtonId -> {
                updateFloorUp()
            }
            downButtonId -> {
                updateFloorDown()
            }
            else -> {
                0
            }
        }
    }
    private fun updateFloorUp(): Int {

        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if (currentFloor >= floorNumbers!![floorNumbers.size - 1]) {
            return floorNumbers!![floorNumbers.size - 1]
        }
        return currentFloor + 1
    }

    private fun updateFloorDown(): Int {
        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if (currentFloor <= floorNumbers!![0]) {
            return floorNumbers!![0]
        }
        return (currentFloor - 1)
    }

    private fun changeVisibleFloor(floorPlans: HashMap <Int, Floor>, updatedFloor: Int) {
        floorPlans?.get(currentFloor)?.hideFloor()
        floorPlans?.get(updatedFloor)?.displayFloor()

        currentFloor = updatedFloor
        changeFloorListener?.invoke(currentFloor)
    }

    fun getCurrentFloor(): Int {
        return currentFloor
    }
}
