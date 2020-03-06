package com.example.campusguide

import com.google.android.gms.maps.model.LatLng

interface Map {
    fun addMarker(position: LatLng, title: String)
    fun animateCamera(position: LatLng, zoom: Float)
}