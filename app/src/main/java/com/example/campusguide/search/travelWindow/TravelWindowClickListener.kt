package com.example.campusguide.search.travelWindow

import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class TravelWindowClickListener(private val directions: DirectionsFlow) : GoogleMap.OnInfoWindowClickListener {
    override fun onInfoWindowClick(p0: Marker?) {
        p0!!.remove()
        val coordinates = p0!!.position
        val location = Location(p0!!.title, coordinates.latitude, coordinates.longitude)

        directions.startFlow(null, location)
    }
}