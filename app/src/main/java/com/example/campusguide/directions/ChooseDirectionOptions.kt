package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChooseDirectionOptions : DialogFragment() {

    private lateinit var route: Route

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.choose_directions, null)

        val builder : AlertDialog.Builder = AlertDialog.Builder(this.activity)
        builder.setView(view)

        val useCurrentLocation = view.findViewById<Button>(R.id.useCurrentLocation)

        view.findViewById<Button>(R.id.useCurrentLocation2)?.setOnClickListener(View.OnClickListener {
            val getDirectionsDialogFragment =
                GetDirectionsDialogFragment(
                    GetDirectionsDialogFragment.DirectionsDialogOptions(
                        null, null,
                        EmptyDirectionsGuard(
                            this.requireActivity() as AppCompatActivity,
                            CallbackDirectionsConfirmListener { start, end ->
                                //Display the directions time
                                route.set(start, end)
                            })
                    )
                )
            getDirectionsDialogFragment.show(childFragmentManager, "directionsDialog")
        })

        return builder.create()
    }
}