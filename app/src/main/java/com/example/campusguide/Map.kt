package com.example.campusguide

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.MarkerOptions

interface Map {
    fun addMarker(options: MarkerOptions)
    fun animateCamera(update: CameraUpdate)
}