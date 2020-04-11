package com.example.campusguide.map.displayIndoor

import android.view.View
import com.example.campusguide.R
import com.example.campusguide.map.GoogleMapAdapter
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.HashMap

class ChangeFloor constructor(private val map: GoogleMapAdapter)
    : View.OnClickListener {
    private var upButtonId = -1
    private var downButtonId = -1
    private var currentFloor = 4
    private val buildings =  HashMap<String, HashMap<Int, GroundOverlay>>()
    private  var floors : HashMap<Int, GroundOverlay>? = null
    private var started = false;
    private val allBuildingCoordinates = createBuildingCoordinatesArray()
    private lateinit var currentBuildingCoordinates : LatLng

    private fun createBuildingCoordinatesArray(): HashMap<String, LatLng>{
        val buildingCenterCoordinates = HashMap<String, LatLng>()
        buildingCenterCoordinates["hall"] = LatLng(45.4972695, -73.57894175)
        buildingCenterCoordinates["library"] = LatLng(45.496782, -73.577889)

        return buildingCenterCoordinates
    }

    object CoordinateHolder {
        val hallCoordinates = LatLng(45.4972695, -73.57894175)
        val libraryCoordinates = LatLng(45.496782, -73.577889)
    }

    fun setIds(upButtonId: Int, downButtonId: Int){
        this.upButtonId = upButtonId
        this.downButtonId = downButtonId
    }
    fun setBuilding(buildingName: String){
        if (buildings.containsKey(buildingName)){
            currentBuildingCoordinates = allBuildingCoordinates[buildingName]!!
            floors = buildings.getValue(buildingName)
        }
        else {
            var buildingFloors = HashMap<Int, GroundOverlay>()
            if (buildingName == "hall"){
                buildingFloors[4] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor2))
                        .position(CoordinateHolder.hallCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))
                buildingFloors[5] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor2))
                        .position(CoordinateHolder.hallCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))
                buildingFloors[6] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor3))
                        .position(CoordinateHolder.hallCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))
                buildingFloors[7] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor4))
                        .position(CoordinateHolder.hallCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))
            }
            else if (buildingName == "library"){
                buildingFloors[2] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor2))
                        .position(CoordinateHolder.libraryCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))
                buildingFloors[3] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor3))
                        .position(CoordinateHolder.libraryCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))

                buildingFloors[4] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor4))
                        .position(CoordinateHolder.libraryCoordinates, 68F, 63F).bearing(124F)
                        .zIndex(-1F)
                        .visible(true))
                buildingFloors[5] = map.adapted.addGroundOverlay(
                    GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor5))
                        .position(CoordinateHolder.libraryCoordinates, 68F, 63F).bearing(-56F)
                        .zIndex(-1F)
                        .visible(true))
            }

            buildings[buildingName] = buildingFloors
            floors = buildingFloors
        }
    }


    override fun onClick(v: View?) {
//        if (!started){
//
//            floors[5] = map.adapted.addGroundOverlay(
//                GroundOverlayOptions()
//                    .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor2))
//                    .position(hallCoordinates, 68F, 63F).bearing(124F)
//                    .zIndex(-1F)
//                    .visible(true)
//            )
//            floors[6] = map.adapted.addGroundOverlay(
//                GroundOverlayOptions()
//                    .image(BitmapDescriptorFactory.fromResource(R.drawable.lb_floor3))
//                    .position(hallCoordinates, 68F, 63F).bearing(124F)
//                    .zIndex(-1F)
//                    .visible(true)
//            )
//
//            this.started = true
//        }

        if (v?.id == upButtonId){
            val updatedFloor = updateFloorUp(currentFloor)
            floors?.get(currentFloor)?.zIndex = -1f
            floors?.get(updatedFloor)?.zIndex = 5f

            map.animateCamera(currentBuildingCoordinates,map.adapted.cameraPosition.zoom)
            currentFloor = updatedFloor
        }
        else if (v?.id == downButtonId){
            val updatedFloor = updateFloorDown(currentFloor)

            floors?.get(currentFloor)?.zIndex = -1f
            floors?.get(updatedFloor)?.zIndex = 5f

            map.animateCamera(currentBuildingCoordinates,map.adapted.cameraPosition.zoom)
            currentFloor = updatedFloor
        }

    }

    fun displayCurrentFloor(){
        if (floors != null){
            floors!![currentFloor]?.zIndex = 5f
        }
        return
    }
    fun hideCurrentFloor(){
        if (floors != null){
            floors!![currentFloor]?.zIndex = -1f
        }

        return
    }
    private fun updateFloorUp(currentFloor: Int): Int {
        if(currentFloor== 8){
            return 8;
        }
        return currentFloor + 1;
    }
    private fun updateFloorDown(currentFloor: Int): Int {
        if(currentFloor== 4){
            return 4;
        }
        return currentFloor - 1;
    }
}