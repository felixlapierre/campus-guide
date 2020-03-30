package com.example.campusguide.search.travelWindow

import com.example.campusguide.directions.DirectionsFlow
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.model.LatLng

class TravelWindowClickListener(private val directions: DirectionsFlow) : GoogleMap.OnInfoWindowClickListener {
    override fun onInfoWindowClick(p0: Marker?) {
        p0!!.remove()
        val coordinates = p0!!.position
        val coordinatesAsLatLng = LatLng(coordinates.latitude, coordinates.longitude)

        directions.startFlow(null, coordinatesAsLatLng.toString())
    }
}