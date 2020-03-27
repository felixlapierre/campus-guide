package com.example.campusguide.map

import android.app.Activity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapClickListener constructor(private val map : GoogleMap, private val activity: Activity) : GoogleMap.OnMapClickListener {

    override fun onMapClick(p0: LatLng?) {
        map.addMarker(p0?.let {
            MarkerOptions()
                .position(it)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        })
        map.moveCamera(CameraUpdateFactory.newLatLng(p0))
    }
}