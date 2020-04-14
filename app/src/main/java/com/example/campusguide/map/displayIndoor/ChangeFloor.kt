package com.example.campusguide.map.displ

import com.example.campusguide.map.displayIndoor.BuildingInfo
import android.view.View
import com.example.campusguide.map.GoogleMapAdapter
import kotlin.collections.HashMap

class ChangeFloor constructor(private val map: GoogleMapAdapter)
    : View.OnClickListener {

    private val buildings =  buildBuildings(map)
    private var currentBuilding : String = ""
    private var currentFloor = -1

    companion object ButtonInfo{
        var upButtonId = -1
        var downButtonId = -1
    }

    private fun buildBuildings(map: GoogleMapAdapter) : HashMap<String, BuildingInfo>{
        val build = HashMap<String, BuildingInfo>()
        build["hall"] = BuildingInfo("hall", map)
        build["library"] = BuildingInfo("library", map)
        return build
    }

    fun setBuilding(buildingName: String){

        if (buildingName != currentBuilding){
            unsetBuilding()
        }
        currentBuilding = buildingName
        currentFloor = buildings[buildingName]?.startFloor!!
        displayCurrentFloor()
    }

    fun unsetBuilding(){
        hideCurrentFloor()
        currentBuilding = ""
        currentFloor = -1

    }

    override fun onClick(v: View?) {

        val buildingImageLatLng = buildings[currentBuilding]?.getBuildingImageCoordinates()

        if (v?.id == upButtonId){
            val updatedFloor = updateFloorUp(currentFloor)

            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = false
            buildings[currentBuilding]?.getFloorPlans()?.get(updatedFloor)?.isVisible = true

            currentFloor = updatedFloor
        }
        else if (v?.id == downButtonId){
            val updatedFloor = updateFloorDown(currentFloor)

            buildings[currentBuilding]?.getFloorPlans()?.get(updatedFloor)?.isVisible = true
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = false
            currentFloor = updatedFloor
        }
        if (buildingImageLatLng != null) {
            map.animateCamera(buildingImageLatLng,map.adapted.cameraPosition.zoom)
        }
    }

    private fun displayCurrentFloor(){
        if (currentBuilding != "" ){
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = true
        }
    }
    private fun hideCurrentFloor(){
        if (currentBuilding != ""){
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = false
        }
    }
    private fun updateFloorUp(currentFloor: Int): Int {

        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if(currentFloor == floorNumbers!![floorNumbers.size-1]
            || currentFloor == buildings[currentBuilding]?.getFloors()!![0]){
            return currentFloor
        }

        return currentFloor + 1
    }
    private fun updateFloorDown(currentFloor: Int): Int {
        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if(currentFloor == floorNumbers!![0]
            || currentFloor == floorNumbers[floorNumbers.size-1] ){
            return currentFloor
        }
        return currentFloor - 1
    }
}