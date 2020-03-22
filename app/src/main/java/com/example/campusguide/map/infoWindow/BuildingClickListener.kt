package com.example.campusguide.map.infoWindow

import CustomInfoWindow
import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*


class BuildingClickListener(private val context: Context, private val googleMap: GoogleMap) : GoogleMap.OnPolygonClickListener{
    private var marker: Marker? = null

    fun buildingInfoWindow(location: LatLng, info: InfoWindowData) {
        val customInfoWindow = CustomInfoWindow(context)
        googleMap.setInfoWindowAdapter(customInfoWindow)

        // Clears any existing markers from the GoogleMap
        marker?.remove()

        // Creating an instance of MarkerOptions to set position
        val markerOptions = MarkerOptions()

        // Setting position on the MarkerOptions
        markerOptions.position(location)

        //Set Custom InfoWindow Adapter
        googleMap.setInfoWindowAdapter(customInfoWindow)

        marker = googleMap.addMarker(markerOptions)
        marker?.tag = info
        marker?.showInfoWindow()

        // Animating to the info window
        val cameraLocation = LatLng((location.latitude)+0.00055, location.longitude)
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(cameraLocation))
    }

    override fun onPolygonClick(p0: Polygon?) {
        val location = getPolygonCenterPoint(p0?.points as ArrayList<LatLng>)
        val info = determineBuilding(location)

        buildingInfoWindow(location!!, info)
    }

    private fun getPolygonCenterPoint(polygonPointsList: ArrayList<LatLng>): LatLng? {
        var centerLatLng: LatLng? = null
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        for (i in 0 until polygonPointsList.size) {
            builder.include(polygonPointsList[i])
        }
        val bounds: LatLngBounds = builder.build()
        centerLatLng = bounds.center
        return centerLatLng
    }

    private fun determineBuilding(location: LatLng?): InfoWindowData {
        val info = InfoWindowData()
        if(location == LatLng(45.49727, -73.5789415)){
            info.symbol = "H"
            info.fullName = "Henry F. Hall Building"
            info.address = "1455 Blvd de Maisonneuve O, Montreal, QC"
            info.services = "Services:"
            info.servicesList = "-Concordia Student Union\n-Engineering and Computer Science Society\n-Student Success Center"
        } else if(location == LatLng(45.496758, -73.57793649999999)){
            info.symbol = "LB"
            info.fullName = "J.W McConnell Building"
            info.address = "1400 Blvd de Maisonneuve O, Montreal, QC"
            info.services = "Services:"
            info.servicesList = "-R. Howard Webster Library\n-Birks Student Center\n-Campus Stores\n-4TH SPACE"
        } else if(location == LatLng(45.495548299999996, -73.57818470000001)) {
            info.symbol = "EV"
            info.fullName = "Engineering, Computer Science and Visual Arts Integrated Complex"
            info.address = "1515 St Catherine W., Montreal, QC"
            info.services = "Services:"
            info.servicesList = "-Le Gym\n-FOFA Gallery"
        } else {
            info.symbol = "B"
            info.fullName = "Building"
            info.address = "location" + (location?.latitude) + ", " + (location?.longitude)
            info.services = "Services:"
            info.servicesList = "-service1\n-service2"
        }
        return info
    }

}
