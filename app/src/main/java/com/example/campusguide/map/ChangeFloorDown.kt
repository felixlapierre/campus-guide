package com.example.campusguide.map
//
//import android.view.View
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.GroundOverlay
//import com.google.android.gms.maps.model.GroundOverlayOptions
//import com.google.android.gms.maps.model.LatLng
//import kotlin.collections.HashMap
//
//class ChangeFloorDown constructor(private val map: GoogleMapAdapter)
//    : View.OnClickListener {
//    private val hallCoordinates = LatLng(45.4972695, -73.57894175)
//    private var currentFloor = 8
//    private var floors = HashMap<Int, GroundOverlay>()
//    private var started = false;
//
//    override fun onClick(v: View?) {
//
//        if (!started){
//            for (floor in 4..8) floors[floor] = map.adapted.addGroundOverlay(
//                GroundOverlayOptions()
//                    .image(BitmapDescriptorFactory.fromAsset("Hall$floor.bmp"))
//                    .position(hallCoordinates, 68F, 63F).bearing(124F)
//                    .zIndex(-1F)
//                    .visible(true)
//            )
//            this.started = true
//        }
//
//        val updatedFloor = updateFloorUp(currentFloor)
//
//        floors[currentFloor]?.zIndex = -1f
//        floors[updatedFloor]?.zIndex = 5f
//
//
//        map.animateCamera(hallCoordinates,map.adapted.cameraPosition.zoom)
//        currentFloor = updatedFloor
//    }
//
//    private fun updateFloorUp(currentFloor: Int): Int {
//        val updatedFloor = currentFloor+1
//
//        if(updatedFloor== 9){
//            return 4;
//        }
//        return updatedFloor;
//    }
//}