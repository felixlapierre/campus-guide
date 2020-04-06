package com.example.campusguide.search.travelWindow

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.campusguide.R
import com.example.campusguide.map.infoWindow.InfoWindowData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class TravelWindow (private val context: Context) : GoogleMap.InfoWindowAdapter{

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = (context as Activity).layoutInflater
            .inflate(R.layout.confirm_window, null)

        val infoWindowData = marker.tag as InfoWindowData?

        marker.title = infoWindowData?.fullName
        view.findViewById<TextView>(R.id.locationName).text = infoWindowData?.fullName
        view.findViewById<TextView>(R.id.secondaryText).text = infoWindowData?.address

        return view
    }
}