package com.example.campusguide

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapAdapter : Map {
    lateinit var adapted: GoogleMap

    override fun addMarker(position: LatLng, title: String) {
        adapted.addMarker(
            MarkerOptions()
                .position(position)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
    }

    override fun animateCamera(position: LatLng, zoom: Float) {
        adapted.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                position,
                zoom
            )
        )
    }
}