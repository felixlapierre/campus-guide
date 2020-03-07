package com.example.campusguide.map

import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import java.util.*

class ChangeFloor constructor(private val map: GoogleMapAdapter)
    : View.OnClickListener {

    private val hallCoordinates = LatLng(45.4972695, -73.57894175)
    private var currentFloor = 4

    override fun onClick(v: View?) {
        var updatedFloor = ++currentFloor

        if(updatedFloor == 9){
            currentFloor = 4
            updatedFloor = 4
        }

        val fileName = "Hall$updatedFloor.bmp"

        map.adapted.addGroundOverlay(
                GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromAsset(fileName))
                    .position(hallCoordinates, 68F, 63F).bearing(124F)
                    .zIndex(5F)
                    .visible(true)
                )
        map.animateCamera(hallCoordinates,map.adapted.cameraPosition.zoom)

    }
}