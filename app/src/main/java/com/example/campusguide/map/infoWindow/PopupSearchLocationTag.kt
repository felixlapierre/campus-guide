package com.example.campusguide.map.infoWindow

import android.view.View
import android.widget.TextView
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.travelWindow.TravelWindowClickListener
import com.google.android.gms.maps.GoogleMap

class PopupSearchLocationTag
    (
    layout: Int,
    private val location: SearchLocation,
    private val directions: DirectionsFlow
): MarkerTag(layout) {
    override fun onInfoWindowClick(): GoogleMap.OnInfoWindowClickListener {
        return TravelWindowClickListener(directions, location)
    }

    override fun onInfoWindowClose(): GoogleMap.OnInfoWindowCloseListener {
        return GoogleMap.OnInfoWindowCloseListener {
            //noop
        }
    }

    override fun fillOutInfor(view: View): View {
        view.findViewById<TextView>(R.id.locationName).text = location.name
        view.findViewById<TextView>(R.id.secondaryText).text = location.secondaryText
        return view
    }
}