package com.example.campusguide.directions

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.utils.permissions.PermissionsSubject

class ChooseOriginOptions(private val permission: PermissionsSubject, private val locationProvider: FusedLocationProvider, private val locationSelectedListener: (location: Location) -> Unit ) : DialogFragment() {

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.choose_origin_options, container, false)

        view.findViewById<Button>(R.id.currentLocation)?.setOnClickListener {
            useCurrentLocation()
        }

        view.findViewById<Button>(R.id.search)?.setOnClickListener {
            searchForLocation()
        }

        return view
    }

    /**
     * Gets the user's last known most recent location
     */
    private fun useCurrentLocation() {
        if(permission.havePermission(locationPermission)){
            dismiss()
            locationProvider.getLocation { location ->
                locationSelectedListener(location)
            }
        }else{
            permission.requestPermission(locationPermission)
        }
    }

    /**
     * Allows users to manually enter start and end point
     */
    private fun searchForLocation() {
        //TODO fill this method once we have the logic to search for a location
        dismiss()
        val location = Location("Montreal")
        location.latitude = 45.5017
        location.longitude = -73.5673
        locationSelectedListener(location)
    }
}