package com.example.campusguide.directions.outdoor

import com.google.android.gms.maps.model.LatLng
import com.google.maps.internal.PolylineEncoding

class OutdoorRoute constructor(private val directions: OutdoorDirections) {
    private var line: List<LatLng> = emptyList()

    suspend fun set(start: String, end: String, travelMode: String) {
        val response = directions.getDirections(start, end, travelMode)

        if(response != null) {
           line = PolylineEncoding.decode(response.routes[0].overviewPolyline.points).map {
               LatLng(it.lat, it.lng)
           }
        }
    }

    fun getLine(): List<LatLng> {
        return line
    }
}