package com.example.campusguide.directions.outdoor

import com.example.campusguide.map.Map
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.internal.PolylineEncoding

class OutdoorRoute constructor(private val directions: OutdoorDirections) {
    private var polylineOptions: PolylineOptions? = null
    private var polyline: Polyline? = null

    suspend fun set(start: String, end: String, travelMode: String) {
        val response = directions.getDirections(start, end, travelMode)

        if(response != null) {
            val line = response.routes[0].overviewPolyline.points
            val decoded = PolylineEncoding.decode(line).map {
                LatLng(it.lat, it.lng)
            }

            polylineOptions = PolylineOptions().addAll(decoded)
        }
    }

    fun display(map: Map) {
        polyline?.remove()
        polyline = map.addPolyline(polylineOptions)
    }
}