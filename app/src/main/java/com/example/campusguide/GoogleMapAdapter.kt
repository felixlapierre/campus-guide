package com.example.campusguide

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapAdapter: Map {
    lateinit var adapted: GoogleMap

    override fun addMarker(options: MarkerOptions) {
        adapted.addMarker(options)
    }

    override fun animateCamera(update: CameraUpdate) {
        adapted.animateCamera(update)
    }
}