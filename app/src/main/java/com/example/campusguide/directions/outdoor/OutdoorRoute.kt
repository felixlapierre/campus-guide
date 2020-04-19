package com.example.campusguide.directions.outdoor

import com.example.campusguide.directions.GoogleDirectionsAPIStep
import com.google.android.gms.maps.model.LatLng
import com.google.maps.internal.PolylineEncoding

class OutdoorRoute constructor(private val directions: OutdoorDirections) {
    private var line: List<LatLng> = emptyList()
    private var steps: List<GoogleDirectionsAPIStep> = emptyList()
    private var duration: Int = 0
    var distance: String = ""
    private var fare: String = ""

    suspend fun set(start: String, end: String, travelMode: String, transitPreference: String?) {
        val response = directions.getDirections(start, end, travelMode, transitPreference)

        if (response != null) {
            line = PolylineEncoding.decode(response.routes[0].overviewPolyline.points).map {
                LatLng(it.lat, it.lng)
           }

            steps = response.routes[0].legs[0].steps
            duration = response.routes[0].legs[0].duration.value
            distance = response.routes[0].legs[0].distance.text
            fare = response.routes[0].fare.text
        }
    }

    fun getLine(): List<LatLng> {
        return line
    }

    fun getDuration(): Int {
        return duration
    }

    fun getSteps(): List<GoogleDirectionsAPIStep> {
        return steps
    }

    fun getFare(): String {
        return fare
    }
}
