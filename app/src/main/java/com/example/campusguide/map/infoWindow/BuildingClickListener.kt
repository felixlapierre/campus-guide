package com.example.campusguide.map.infoWindow

import CustomInfoWindow
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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
        val location = getPolygonCenterPoint(p0?.points as ArrayList<LatLng>)
        val info = InfoWindowData()

        info.symbol = "B"
        info.fullName = "Building"
        info.address = "location" + (location?.latitude) + ", " + (location?.longitude)
        info.services = "some"
        info.events = "Events"

        buildingInfoWindow(location!!, info)
    }

    private fun getPolygonCenterPoint(polygonPointsList: ArrayList<LatLng>): LatLng? {
        var centerLatLng: LatLng? = null
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        for (i in 0 until polygonPointsList.size) {
            builder.include(polygonPointsList[i])
        }
        val bounds: LatLngBounds = builder.build()
        centerLatLng = bounds.center
        return centerLatLng
    }

}
