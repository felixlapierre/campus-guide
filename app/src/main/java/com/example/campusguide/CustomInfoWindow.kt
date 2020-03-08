package com.example.campusguide

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindow(val context: Context) : GoogleMap.InfoWindowAdapter{

    override fun getInfoContents(p0: Marker?): View {
        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.mew_custom_map_marker, null)
        return mInfoView
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

}