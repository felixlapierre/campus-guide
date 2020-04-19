package com.example.campusguide.map.displayIndoor

import android.view.View
import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.model.GroundOverlay
import kotlin.collections.HashMap

class ChangeFloor constructor(private val map: GoogleMapAdapter) : View.OnClickListener {

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
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = isVisible
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

    private fun changeVisibleFloor(floorPlans: HashMap <Int, GroundOverlay>, updatedFloor: Int) {
        floorPlans?.get(currentFloor)?.isVisible = false
        floorPlans?.get(updatedFloor)?.isVisible = true

        currentFloor = updatedFloor
        changeFloorListener?.invoke(currentFloor)
    }

    fun getCurrentFloor(): Int {
        return currentFloor
    }
}
