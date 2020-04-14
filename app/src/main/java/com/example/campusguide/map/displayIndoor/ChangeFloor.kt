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
    companion object buttonInfo{
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

        currentBuilding = ""
        currentFloor = -1
        hideCurrentFloor()
    }

    override fun onClick(v: View?) {

        val buildingImageLatLng = buildings[currentBuilding]?.getBuildingImageCoordinates()

        if (v?.id == upButtonId){
            val updatedFloor = updateFloorUp(currentFloor)

            buildings[currentBuilding]?.getFloorPlans()?.get(updatedFloor)?.isVisible = true
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = false

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

    fun displayCurrentFloor(){
        if (currentBuilding != "" && buildings != null){
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = true
        }
    }
    fun hideCurrentFloor(){
        if (currentBuilding != "" && buildings != null){
            buildings[currentBuilding]?.getFloorPlans()?.get(currentFloor)?.isVisible = false
        }
    }
    private fun updateFloorUp(currentFloor: Int): Int {

        val floorNumbers = buildings[currentBuilding]?.getFloors()
        if(currentFloor == floorNumbers!![floorNumbers.size-1]){
            return currentFloor;
        }
        return currentFloor + 1;
    }
    private fun updateFloorDown(currentFloor: Int): Int {

        if(currentFloor == buildings[currentBuilding]?.getFloors()!![0]){
            return currentFloor;
        }
        return currentFloor - 1;
    }
}