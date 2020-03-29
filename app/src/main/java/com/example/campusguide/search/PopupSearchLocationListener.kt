package com.example.campusguide.search

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.Constants
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.infoWindow.InfoWindowData
import com.example.campusguide.search.travelWindow.TravelWindow
import com.example.campusguide.search.travelWindow.TravelWindowClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PopupSearchLocationListener constructor(private val activity: AppCompatActivity, private val directions: DirectionsFlow, private val map: GoogleMapAdapter): SearchLocationListener {
    private var marker: com.example.campusguide.map.Marker? = null

    override fun onLocation(location: SearchLocation) {
        activity.runOnUiThread {
            val travelWindow = TravelWindow(activity)
            map.setInfoWindowAdapter(travelWindow)
            map.setOnInfoWindowClickListener(TravelWindowClickListener(directions))

            val infoWindowData = InfoWindowData()
            infoWindowData.fullName = location.name
            infoWindowData.address = location.secondaryText

            marker?.remove()

            val latLng = LatLng(location.lat, location.lon)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)

            map.setInfoWindowAdapter(travelWindow)

            marker = map.addMarker(markerOptions)
            marker?.setTag(infoWindowData)
            marker?.showInfoWindow()
            map.animateCamera(latLng, Constants.ZOOM_STREET_LVL)
        }
    }
}