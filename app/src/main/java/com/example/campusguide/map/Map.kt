package com.example.campusguide.map

import com.example.campusguide.directions.PathPolyline
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

interface Map {
    fun addMarker(position: LatLng, title: String): Marker?
    fun addMarker(opts: MarkerOptions): Marker?
    fun animateCamera(position: LatLng, zoom: Float)
    fun moveCamera(newLatLngZoom: CameraUpdate?)
    fun moveCamera(bounds: LatLngBounds)
    fun addPolyline(polyOptions: PolylineOptions?): Polyline?
    fun addPath(path: PathPolyline)
    fun setInfoWindowAdapter(infoWindowAdapter: GoogleMap.InfoWindowAdapter)
    fun setOnInfoWindowClickListener(infoWindowClickListener: GoogleMap.OnInfoWindowClickListener)
    fun setOnInfoWindowCloseListener(infoWindowCloseListener: GoogleMap.OnInfoWindowCloseListener)
}
