package com.example.campusguide.map.infoWindow

import android.view.View
import android.widget.TextView
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.search.indoor.IndoorLocation
import com.example.campusguide.search.indoor.Room
import com.google.android.gms.maps.GoogleMap

class IndoorTag(
    layout: Int,
    private val directionsFlow: DirectionsFlow,
    private val room: Room,
    private val roomId: String
) : MarkerTag(layout) {
    override fun onInfoWindowClick(): GoogleMap.OnInfoWindowClickListener {
        return GoogleMap.OnInfoWindowClickListener {
            val location = IndoorLocation(room.code, room.lat.toDouble(), room.lon.toDouble(), roomId, room.name)
            directionsFlow.startFlow(destination = location)
        }
    }

    override fun onInfoWindowClose(): GoogleMap.OnInfoWindowCloseListener {
        return GoogleMap.OnInfoWindowCloseListener {
            // noop
        }
    }

    override fun fillView(view: View): View {
        view.findViewById<TextView>(R.id.locationName).text = room.code
        view.findViewById<TextView>(R.id.secondaryText).text = room.name
        return view
    }
}
