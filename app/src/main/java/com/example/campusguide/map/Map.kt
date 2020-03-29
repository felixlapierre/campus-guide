package com.example.campusguide.map

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

interface Map {
    fun addMarker(position: LatLng, title: String): Marker?
    fun addMarker(opts: MarkerOptions): Marker?
    fun animateCamera(position: LatLng, zoom: Float)
    fun moveCamera(newLatLngZoom: CameraUpdate?)
    fun addPolyline(polyOptions: PolylineOptions?): Polyline?
    fun setInfoWindowAdapter(adapter: GoogleMap.InfoWindowAdapter)
}