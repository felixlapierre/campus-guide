package com.example.campusguide.directions.outdoor

import com.example.campusguide.map.Map
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class OutdoorRoute constructor(private val directions: OutdoorDirections) {
    private var polylineOptions: PolylineOptions? = null
    private var polyline: Polyline? = null

    suspend fun set(start: String, end: String, travelMode: String) {
        val response = directions.getDirections(start, end, travelMode)

        if(response != null) {
            val firstRoute = response.routes[0]
            val listOfPoints = firstRoute.legs.map { leg ->
                LatLng(leg.startLocation.lat.toDouble(), leg.startLocation.lng.toDouble())
            }.toMutableList()
            val lastLocation = firstRoute.legs.get(firstRoute.legs.size - 1).endLocation
            listOfPoints.add(LatLng(lastLocation.lat.toDouble(), lastLocation.lng.toDouble()))

            polylineOptions = PolylineOptions().addAll(listOfPoints)
        }
    }

    fun display(map: Map) {
        polyline?.remove()
        polyline = map.addPolyline(polylineOptions)
    }
}