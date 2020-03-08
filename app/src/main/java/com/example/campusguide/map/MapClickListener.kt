package com.example.campusguide.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapClickListener constructor(private val map : GoogleMap) : GoogleMap.OnMapClickListener {

    override fun onMapClick(p0: LatLng?) {
        val marker = map.addMarker(p0?.let {
            MarkerOptions()
                .position(it)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        })
        marker.showInfoWindow()
        map.moveCamera(CameraUpdateFactory.newLatLng(p0))
    }
}