package com.example.campusguide

import com.google.android.gms.maps.model.LatLng

interface ap {
    fun addMarker(position: LatLng, title: String)
    fun animateCamera(position: LatLng, zoom: Float)
}