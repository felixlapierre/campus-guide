package com.example.campusguide.map.infoWindow

import CustomInfoWindow
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon

class BuildingClickListener(private val context: Context, private val googleMap: GoogleMap) : GoogleMap.OnPolygonClickListener{

    fun buildingInfoWindow(location: LatLng, info: InfoWindowData) {
        val customInfoWindow = CustomInfoWindow(context)
        googleMap.setInfoWindowAdapter(customInfoWindow)

        // Clears any existing markers from the GoogleMap
        //googleMap.clear()

        // Creating an instance of MarkerOptions to set position
        val markerOptions = MarkerOptions()

        // Setting position on the MarkerOptions
        markerOptions.position(location)

        //Set Custom InfoWindow Adapter
        googleMap.setInfoWindowAdapter(customInfoWindow)

        // Animating to the currently touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))

        val marker = googleMap.addMarker(markerOptions)
        marker.tag = info
        marker.showInfoWindow()
    }

    override fun onPolygonClick(p0: Polygon?) {

        val location = p0?.points?.get(0)
        val info = InfoWindowData()

        info.symbol = "B"
        info.full_name = "Building"
        info.address = "location" + (location?.latitude) + ", " + (location?.longitude)
        info.services = "some"
        info.events = "Events"

        buildingInfoWindow(location!!, info)
    }

}
