package com.example.campusguide.map.infoWindow

import com.example.campusguide.Constants
import com.example.campusguide.directions.DirectionsFlow
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
        val location = PolygonUtils().getPolygonCenterPoint(points)
        val info = determineBuilding(location)

        buildingInfoWindow(location!!, info)
    }

    private fun determineBuilding(location: LatLng?): InfoWindowData {
        val info = InfoWindowData()
        var building = location?.let { index.getBuildingAtCoordinates(it) }
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

    private fun buildingInfoWindow(location: LatLng, info: InfoWindowData) {
        map.setInfoWindowAdapter(customInfoWindow)
        map.setInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            directions?.startFlow(null, info.fullName)
        })

        // Clears any existing markers from the GoogleMap
        marker?.remove()

        // Creating an instance of MarkerOptions to set position
        val markerOptions = MarkerOptions()

        // Setting position on the MarkerOptions
        markerOptions.position(location)

        marker = map.addMarker(markerOptions)
        marker?.setTag(info)
        marker?.showInfoWindow()

        // Animating to the info window
        val cameraLocation = LatLng((location.latitude) + 0.00055, location.longitude)
        map.animateCamera(cameraLocation, Constants.ZOOM_STREET_LVL)
    }
}
