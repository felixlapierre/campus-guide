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

        view.findViewById<Button>(R.id.cafe)?.setOnClickListener {
            dismiss();
            searchPOINearCurrentLocation("Cafe");
        }
        // view.findViewById<Button>(R.id.fastfood)?.setOnClickListener {
        //     dismiss();
        //     searchPOINearCurrentLocation("Fast Food");
        // }
        // view.findViewById<Button>(R.id.restaurant)?.setOnClickListener {
        //     dismiss();
        //     searchPOINearCurrentLocation("Restaurant");
        // }
        // view.findViewById<Button>(R.id.shopping)?.setOnClickListener {
        //     dismiss();
        //     searchPOINearCurrentLocation("Shopping");
        // }
        // view.findViewById<Button>(R.id.pharmacy)?.setOnClickListener {
        //     dismiss();
        //     searchPOINearCurrentLocation("Pharmacy");
        // }
        return view
    }

    /**
     * Gets the user's last known most recent location
     */

    private fun searchPOINearCurrentLocation(poi: String) {
        return locationProvider.getLocation {
            getNearbyPlaces(poi, it)
        }
    }

    private fun getNearbyPlaces(poi: String, location : Location) {
        nearbyLocations.searchNearbyPlaces(poi, location, map)
    }

}