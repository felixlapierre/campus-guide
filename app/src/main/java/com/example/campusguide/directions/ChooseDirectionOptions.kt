package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R

class ChooseDirectionOptions constructor(private val route : Route?): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.choose_directions, null)

        val builder : AlertDialog.Builder = AlertDialog.Builder(this.activity)
        builder.setView(view)

        view.findViewById<Button>(R.id.useCurrentLocation2)?.setOnClickListener(View.OnClickListener {
            useCurrentLocation()
        })

        view.findViewById<Button>(R.id.searchForLocation2)?.setOnClickListener(View.OnClickListener {
            searchForLocation()
        })

        val dialog : AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return builder.create()
    }

    private fun useCurrentLocation() {
        openDirectionsDialogFragment("Hall", "Enter end location")
    }

    private fun searchForLocation() {
        openDirectionsDialogFragment(null, "Enter start and end location")
    }

    private fun openDirectionsDialogFragment(startingPoint: String?, message: String) {
        val getDirectionsDialogFragment =
            GetDirectionsDialogFragment(
                GetDirectionsDialogFragment.DirectionsDialogOptions(
                    message, startingPoint, null,
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

    private fun getCurrentLocation() {

    }
}