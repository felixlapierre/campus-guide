package com.example.campusguide.map.infoWindow

import com.example.campusguide.Constants
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.location.Location
import com.example.campusguide.map.Map
import com.example.campusguide.map.Marker
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.utils.PolygonUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon

class BuildingClickListener(
    private val map: Map,
    private val index: BuildingIndex,
    private val customInfoWindow: GoogleMap.InfoWindowAdapter,
    private val directions: DirectionsFlow?
) : GoogleMap.OnPolygonClickListener {
    private var marker: Marker? = null

    override fun onPolygonClick(p0: Polygon?) {
        polygonClick(p0?.points as ArrayList<LatLng>)
    }

    fun polygonClick(points: ArrayList<LatLng>) {
        val coordinates = PolygonUtils().getPolygonCenterPoint(points)
        val info = determineBuilding(coordinates)

        buildingInfoWindow(coordinates!!, info)
    }

    private fun determineBuilding(coordinates: LatLng?): InfoWindowData {
        val info = InfoWindowData()
        var building = coordinates?.let { index.getBuildingAtCoordinates(it) }
        if (building == null) {
            info.symbol = "B"
            info.fullName = "Building"
            info.address = "123 address"
            info.servicesList = "-service1\n-service2"
        } else {
            info.symbol = building.code
            info.fullName = building.name
            info.address = building.address
            info.servicesList = building.services
        }
        info.services = "Services:"
        return info
    }

    private fun buildingInfoWindow(coordinates: LatLng, info: InfoWindowData) {
        map.setInfoWindowAdapter(customInfoWindow)
        map.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            val location = Location(info.fullName!!, coordinates.latitude, coordinates.longitude)
            directions?.startFlow(null, location)
        })

        // Clears any existing markers from the GoogleMap
        marker?.remove()

        // Creating an instance of MarkerOptions to set position
        val markerOptions = MarkerOptions()

        // Setting position on the MarkerOptions
        markerOptions.position(coordinates)

        marker = map.addMarker(markerOptions)
        marker?.setTag(info)
        marker?.showInfoWindow()

        // Animating to the info window
        val cameraLocation = LatLng((coordinates.latitude) + 0.00055, coordinates.longitude)
        map.animateCamera(cameraLocation, Constants.ZOOM_STREET_LVL)
    }
}
