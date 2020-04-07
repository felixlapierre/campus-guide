package com.example.campusguide.directions.outdoor

import com.google.android.gms.maps.model.LatLng
import com.google.maps.internal.PolylineEncoding

class OutdoorRoute constructor(private val directions: OutdoorDirections) {
    private var line: List<LatLng> = emptyList()
    var duration: Int = 0

    suspend fun set(start: String, end: String, travelMode: String) {
        val response = directions.getDirections(start, end, travelMode)

        if(response != null) {
           line = PolylineEncoding.decode(response.routes[0].overviewPolyline.points).map {
               LatLng(it.lat, it.lng)
           }
            duration = response.routes[0].legs[0].duration.value
        }
    }

    fun getLine(): List<LatLng> {
        return line
    }
}