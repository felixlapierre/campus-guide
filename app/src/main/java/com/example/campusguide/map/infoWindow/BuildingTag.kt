package com.example.campusguide.map.infoWindow

import android.text.Layout
import android.view.View
import android.widget.TextView
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class BuildingTag(
    layout: Int,
    private val directions: DirectionsFlow,
    private val coordinates: LatLng,
    var symbol: String = "B",
    var fullName: String = "Building",
    var address: String = "123 Street, Montreal, QC",
    var departments: String = "Departments:",
    var departmentsList: String = "- Faculty 1\n- Faculty 2",
    var services: String = "Services:",
    var servicesList: String = "- Service 1\n- Service 2"
): MarkerTag(layout) {
    override fun onInfoWindowClick(): GoogleMap.OnInfoWindowClickListener{
        return GoogleMap.OnInfoWindowClickListener {
            val location = Location(fullName, coordinates.latitude, coordinates.longitude)
            directions?.startFlow(null, location)
        }
    }

    override fun onInfoWindowClose(): GoogleMap.OnInfoWindowCloseListener {
        return GoogleMap.OnInfoWindowCloseListener {
            it?.remove()
        }
    }

    override fun fillOutInfor(view: View): View {
        view.findViewById<TextView>(R.id.symbol).text = symbol
        view.findViewById<TextView>(R.id.fullName).text = fullName
        view.findViewById<TextView>(R.id.address).text = address
        view.findViewById<TextView>(R.id.departments).text = departments
        view.findViewById<TextView>(R.id.departmentsList).text = departmentsList
        view.findViewById<TextView>(R.id.services).text = services
        view.findViewById<TextView>(R.id.servicesList).text = servicesList

        return view
    }
}