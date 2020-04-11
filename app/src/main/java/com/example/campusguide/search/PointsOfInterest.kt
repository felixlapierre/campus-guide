package com.example.campusguide.search

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.location.Location
import com.example.campusguide.location.LocationProvider
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.outdoor.PlacesApiSearchResultProvider

class PointsOfInterest(
    activity: MapsActivity,
    private val locationProvider: LocationProvider,
    private val map: GoogleMapAdapter
) : DialogFragment() {


    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private val nearbyLocations = PlacesApiSearchResultProvider(activity)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.points_of_interest_options, container, false)

        view.findViewById<Button>(R.id.GetNearbyPlaces)?.setOnClickListener {
            useCurrentLocation();
        }
        return view
    }

    /**
     * Gets the user's last known most recent location
     */

    private fun useCurrentLocation() {
        return locationProvider.getLocation {
            getNearbyPlaces(it)
        }
    }

    private fun getNearbyPlaces(location : Location) {
        nearbyLocations.searchNearbyPlaces(location, map)
    }

}