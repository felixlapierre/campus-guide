package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment

import com.example.campusguide.R
import com.example.campusguide.utils.DisplayMessageErrorListener

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener

class ChooseOriginOptions constructor(private val route: Route?) : DialogFragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.choose_directions, container, false)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.findViewById<Button>(R.id.useCurrentLocation)?.setOnClickListener(View.OnClickListener {
            useCurrentLocation()
        })

        dialog.findViewById<Button>(R.id.searchForLocation)?.setOnClickListener(View.OnClickListener {
            searchForLocation()
        })

        return dialog
    }

    /**
     * Gets the user's last known most recent location
     */
    private fun useCurrentLocation() {
        val locationResult = fusedLocationProviderClient.lastLocation

        locationResult.addOnCompleteListener(
            this.requireActivity(),
            OnCompleteListener<Location?> { task ->
                if (task.isSuccessful) {
                    openDirectionsDialogFragment(
                        task.result?.latitude.toString() + ", " + task.result?.longitude.toString(),
                        "Enter end location"
                    )
                } else {
                    Log.d("NullCurrentLocation", "Current location is null")
                }
            })
    }

    /**
     * Allows users to manually enter start and end point
     */
    private fun searchForLocation() {
        openDirectionsDialogFragment(null, "Enter start and end location")
    }

    /**
     * Opens the GetDirectionsDialogFragment
     */
    private fun openDirectionsDialogFragment(startingPoint: String?, message: String) {
        val getDirectionsDialogFragment =
            GetDirectionsDialogFragment(
                GetDirectionsDialogFragment.DirectionsDialogOptions(
                    message,
                    startingPoint,
                    null,
                    EmptyDirectionsGuard(
                        CallbackDirectionsConfirmListener { start, end ->
                            //Display the directions time
                            route?.set(start, end)
                        },
                        DisplayMessageErrorListener(this.requireActivity()))
                )
            )
        getDirectionsDialogFragment.show(childFragmentManager, "directionsDialog")
    }
}