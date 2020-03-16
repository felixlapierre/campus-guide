package com.example.campusguide.directions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.maps.model.LatLng

class ChooseOriginOptions constructor(private val route: Route?, private val locationSelectedListener: (location: LatLng) -> Unit ) : DialogFragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.choose_origin_options, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

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
        val locationResult = fusedLocationProviderClient.lastLocation

        locationResult.addOnCompleteListener( this.requireActivity() ) { task ->
            if (task.isSuccessful) {
                this.dismiss()
                locationSelectedListener(LatLng(task.result!!.latitude, task.result!!.longitude));
            } else {
                Log.d("NullCurrentLocation", "Current location is null")
            }
        }
    }

    /**
     * Allows users to manually enter start and end point
     */
    private fun searchForLocation() {
        //openDirectionsDialogFragment(null, "Enter start and end location")
        dismiss()
        locationSelectedListener(LatLng(0.0, 0.0));
    }

    /**
     * Opens the GetDirectionsDialogFragment
     */
    private fun openDirectionsDialogFragment(startingPoint: String?, message: String) {

    }
}