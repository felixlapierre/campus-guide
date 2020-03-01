package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener


class ChooseDirectionOptions constructor(private val route: Route?) : DialogFragment() {

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

        view.findViewById<Button>(R.id.useCurrentLocation2)
            ?.setOnClickListener(View.OnClickListener {
                useCurrentLocation()
            })

        view.findViewById<Button>(R.id.searchForLocation2)
            ?.setOnClickListener(View.OnClickListener {
                searchForLocation()
            })

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return builder.create()
    }

    private fun useCurrentLocation() {
        getDeviceLocation()
    }

    private fun searchForLocation() {
        openDirectionsDialogFragment(null, "Enter start and end location")
    }

    private fun openDirectionsDialogFragment(startingPoint: String?, message: String) {
        val getDirectionsDialogFragment =
            GetDirectionsDialogFragment(
                GetDirectionsDialogFragment.DirectionsDialogOptions(
                    message,
                    startingPoint,
                    null,
                    EmptyDirectionsGuard(
                        this.requireActivity() as AppCompatActivity,
                        CallbackDirectionsConfirmListener { start, end ->
                            //Display the directions time
                            route?.set(start, end)
                        })
                )
            )
        getDirectionsDialogFragment.show(childFragmentManager, "directionsDialog")
    }

    /**
     * Gets the user's last known most recent location
     */
    private fun getDeviceLocation() {
        val locationResult = mFusedLocationProviderClient.lastLocation
        var currentLoc: String? = null
        locationResult.addOnCompleteListener(
            this.requireActivity(),
            OnCompleteListener<Location?> { task ->
                if (task.isSuccessful) {
                    openDirectionsDialogFragment(
                        task.result?.latitude.toString() + ", " + task.result?.longitude.toString(),
                        "Enter end location"
                    )
                } else {
                    Log.d("", "Current location is null")
                    Log.e("YES", "Exception: %s", task.exception)
                }
            })
    }
}