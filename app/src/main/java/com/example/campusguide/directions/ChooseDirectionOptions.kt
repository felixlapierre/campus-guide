package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import androidx.fragment.app.DialogFragment

import com.example.campusguide.DirectionsActivity
import com.example.campusguide.R
import com.example.campusguide.utils.DisplayMessageErrorListener

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener

class ChooseDirectionOptions : DialogFragment() {

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    /**
     * Called when the dialog window is created.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.choose_directions, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<Button>(R.id.useCurrentLocation)?.setOnClickListener(View.OnClickListener {
            useCurrentLocation()
        })

        view.findViewById<Button>(R.id.searchForLocation)?.setOnClickListener(View.OnClickListener {
            searchForLocation()
        })

        return builder.create()
    }

    /**
     * Gets the user's last known most recent location
     */
    private fun useCurrentLocation() {
        val locationResult = mFusedLocationProviderClient.lastLocation

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
                    message = message,
                    start = startingPoint,
                    end = null,
                    confirmationListener = EmptyDirectionsGuard(
                        CallbackDirectionsConfirmListener { start, end ->
                            // Start the DirectionsActivity activity
                            val intent = Intent(this.context, DirectionsActivity::class.java).apply {
                                putExtra("Origin", start)
                                putExtra("Destination", end)
                            }
                            startActivity(intent)
                            this.dismiss()
                        },
                        DisplayMessageErrorListener(this.requireActivity()))
                )
            )
        getDirectionsDialogFragment.show(childFragmentManager, "directionsDialog")
    }
}