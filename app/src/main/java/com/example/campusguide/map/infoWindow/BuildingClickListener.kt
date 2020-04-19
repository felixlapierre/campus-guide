package com.example.campusguide.map.infoWindow

import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.map.IMarker
import com.example.campusguide.map.Map
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.utils.PolygonUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon

class BuildingClickListener(
    private val map: Map,
    private val index: BuildingIndex,
    private val directions: DirectionsFlow?
) : GoogleMap.OnPolygonClickListener {
    private var marker: IMarker? = null

    override fun onPolygonClick(p0: Polygon?) {
        polygonClick(p0?.points as ArrayList<LatLng>)
    }

    fun polygonClick(points: ArrayList<LatLng>) {
        val coordinates = PolygonUtils().getPolygonCenterPoint(points)
        val info = determineBuilding(coordinates)

        buildingInfoWindow(coordinates!!, info)
    }

    private fun determineBuilding(coordinates: LatLng?): BuildingTag {
        val buildingTag = BuildingTag(R.layout.custom_info_marker, directions!!, coordinates!!)
        var building = coordinates?.let { index.getBuildingAtCoordinates(it) }
        if (building != null) {
            campusBuilding(buildingTag, building)
        }
        return buildingTag
    }

    private fun buildingInfoWindow(coordinates: LatLng, buildingTag: BuildingTag) {
        // Clears any existing markers from the GoogleMap
        marker?.remove()

        // Creating an instance of MarkerOptions to set position
        val markerOptions = MarkerOptions()

        // Setting position on the MarkerOptions
        markerOptions.position(coordinates)

        marker = map.addMarker(markerOptions)
        marker?.setTag(buildingTag)
        marker?.showInfoWindow()

        // Animating to the info window
        val cameraLocation = LatLng((coordinates.latitude) + 0.0018, coordinates.longitude)
        map.animateCamera(cameraLocation, Constants.ZOOM_STREET_LVL)
    }

    private fun campusBuilding(buildingTag: BuildingTag, building: Building) {
        buildingTag.symbol = building.code
        buildingTag.fullName = building.name
        buildingTag.address = building.address
        buildingTag.departmentsList = building.departments
        buildingTag.servicesList = building.services
    }
}
