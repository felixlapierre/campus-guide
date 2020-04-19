package com.example.campusguide.search

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.Marker
import com.example.campusguide.map.infoWindow.PopupSearchLocationTag
import com.example.campusguide.search.travelWindow.TravelWindowClickListener
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PopupSearchLocationListener constructor(
    private val activity: AppCompatActivity,
    private val directions: DirectionsFlow,
    private val map: GoogleMapAdapter
) : SearchLocationListener {
    private var marker: com.example.campusguide.map.Marker? = null

    companion object {
        private var allMarkers: ArrayList<Marker?> = ArrayList()

        fun clearAllMarkers() {
            allMarkers.forEach {
                it?.remove()
            }
        }
    }

    override fun onLocation(location: SearchLocation?) {
        if (location == null) return
        activity.runOnUiThread {
            map.setOnInfoWindowClickListener(TravelWindowClickListener(directions, location))

            marker?.remove()

            val latLng = LatLng(location.lat, location.lon)
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(location.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

            val popupSearchTag = PopupSearchLocationTag(
                R.layout.confirm_window,
                location,
                directions
            )
            marker = map.addMarker(markerOptions)
            marker?.setTag(popupSearchTag)
            marker?.showInfoWindow()
            map.animateCamera(latLng, Constants.ZOOM_STREET_LVL)

            marker?.let { allMarkers.add(it) }
        }
    }
}
