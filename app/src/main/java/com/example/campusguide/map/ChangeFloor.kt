package com.example.campusguide.map

import android.view.View
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import kotlin.collections.HashMap

class ChangeFloor constructor(private val map: GoogleMapAdapter)
    : View.OnClickListener {
    private var upButtonId = -1
    private var downButtonId = -1
    private val hallCoordinates = LatLng(45.4972695, -73.57894175)
    private var currentFloor = 4
    private var floors = HashMap<Int, GroundOverlay>()
    private var started = false;

    fun setIds(upButtonId: Int, downButtonId: Int){
        this.upButtonId = upButtonId
        this.downButtonId = downButtonId
    }

    override fun onClick(v: View?) {
        if (!started){
            for (floor in 4..8) floors[floor] = map.adapted.addGroundOverlay(
                GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromAsset("Hall$floor.bmp"))
                    .position(hallCoordinates, 68F, 63F).bearing(124F)
                    .zIndex(-1F)
                    .visible(true)
            )
            this.started = true
        }
        if (v?.id == upButtonId){
            val updatedFloor = updateFloorUp(currentFloor)

            floors[currentFloor]?.zIndex = -1f
            floors[updatedFloor]?.zIndex = 5f


            map.animateCamera(hallCoordinates,map.adapted.cameraPosition.zoom)
            currentFloor = updatedFloor
        }
        else if (v?.id == downButtonId){
            val updatedFloor = updateFloorDown(currentFloor)

            floors[currentFloor]?.zIndex = -1f
            floors[updatedFloor]?.zIndex = 5f


            map.animateCamera(hallCoordinates,map.adapted.cameraPosition.zoom)
            currentFloor = updatedFloor
        }

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