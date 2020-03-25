package com.example.campusguide.map.infoWindow

import CustomInfoWindow
import android.content.Context
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.utils.PolygonUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*


class BuildingClickListener(private val context: Context, private val map: Map, private val index: BuildingIndex) : GoogleMap.OnPolygonClickListener{
    private var marker: Marker? = null

    override fun onPolygonClick(p0: Polygon?) {
        val location = PolygonUtils().getPolygonCenterPoint(p0?.points as ArrayList<LatLng>)
        val info = determineBuilding(location)

        buildingInfoWindow(location!!, info)
    }

    fun determineBuilding(location: LatLng?): InfoWindowData {
        val info = InfoWindowData()
        var building = location?.let { index.getBuildingAtCoordinates(it) }
        if(building == null) {
            info.symbol = "B"
            info.fullName = "Building"
            info.address = "location" + (location?.latitude) + ", " + (location?.longitude)
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
        val customInfoWindow = CustomInfoWindow(context)
        map.setInfoWindowAdapter(customInfoWindow)

        // Clears any existing markers from the GoogleMap
        marker?.remove()

        // Creating an instance of MarkerOptions to set position
        val markerOptions = MarkerOptions()

        // Setting position on the MarkerOptions
        markerOptions.position(location)

        marker = map.addMarker(markerOptions)
        marker?.tag = info
        marker?.showInfoWindow()

        // Animating to the info window
        val cameraLocation = LatLng((location.latitude)+0.00055, location.longitude)
        map.animateCamera(cameraLocation, Constants.ZOOM_STREET_LVL)
    }
}
