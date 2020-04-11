package com.example.campusguide.map

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class GoogleMapAdapter : Map {
    lateinit var adapted: GoogleMap

    override fun addMarker(position: LatLng, title: String): Marker? {
        val opts = MarkerOptions()
            .position(position)
            .title(title)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

        return adapted.addMarker(opts)
    }

    override fun addMarker(opts: MarkerOptions): Marker? {
        return adapted.addMarker(opts)
    }

    override fun animateCamera(position: LatLng, zoom: Float) {
        adapted.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                position,
                zoom
            )
        )
    }

    override fun moveCamera(update: CameraUpdate?) {
        return adapted.moveCamera(update)
    }

    override fun addPolyline(polyOptions: PolylineOptions?): Polyline? {
        return adapted.addPolyline(polyOptions)
    }

    fun getCameraZoom(): Float {
        return adapted.cameraPosition.zoom
    }

    fun getCameraLocation(): LatLng{
        return adapted.cameraPosition.target
    }

    fun setCameraMoveListener(cameraMoveListener: GoogleMap.OnCameraMoveListener){
        adapted.setOnCameraMoveListener(cameraMoveListener)
    }
}