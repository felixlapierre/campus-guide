package com.example.campusguide.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class PolygonUtils {
    fun getPolygonCenterPoint(polygonPointsList: ArrayList<LatLng>): LatLng? {
        var centerLatLng: LatLng? = null
        val builder: LatLngBounds.Builder = LatLngBounds.Builder()
        for (i in 0 until polygonPointsList.size) {
            builder.include(polygonPointsList[i])
        }
        val bounds: LatLngBounds = builder.build()
        centerLatLng = bounds.center
        return centerLatLng
    }
}