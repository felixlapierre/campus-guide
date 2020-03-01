package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R
import com.google.android.gms.location.FusedLocationProviderClient


/**
 * An Android fragment that contains a dialog window prompting the user to
 * enter their start and end location for getting directions.
 */
class GetDirectionsDialogFragment constructor(private val options: DirectionsDialogOptions) :
    DialogFragment() {

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    interface DirectionsDialogConfirmationListener {
        fun onConfirm(start: String, end: String)
    }

    /**
     * Options for creating a Directions dialog.
     * @param start Autofills the start location to the specified value
     * @param end Autofills the end location to the specified value
     * @param onConfirm Callback that will be executed when the user confirms the dialog window
     */
    class DirectionsDialogOptions constructor(
        val start: String?, val end: String?,
        val confirmationListener: DirectionsDialogConfirmationListener
    )

    /**
     * Called when the dialog window is created.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Creates a scope where the current activity is bound to the variable "it"
        return activity?.let {

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.directions_dialog_layout, null)

//            setDefaultLocations(view)

            view.findViewById<Button>(R.id.useCurrentLocation)?.setOnClickListener(View.OnClickListener {
                //getCurrentLocation()
            })

            view.findViewById<Button>(R.id.searchForLocation)?.setOnClickListener(View.OnClickListener {

            })

            val builder = AlertDialog.Builder(it)
                .setView(view)
//                .setMessage("Enter start and end location")
//                .setPositiveButton("Go") { _, _ ->
//                    val startEditText =
//                        dialog?.findViewById<EditText>(R.id.startLocationTextInput)
//                    val endEditText = dialog?.findViewById<EditText>(R.id.endLocationTextInput)
//
//                    val start = startEditText?.text.toString()
//                    val end = endEditText?.text.toString()
//
//                    options.confirmationListener.onConfirm(start, end)
//                }
//                .setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.cancel()
//                }

            val dialog : AlertDialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    private fun setDefaultLocations(view: View) {
//        setDefaultText(view, R.id.startLocationTextInput, options.start)
//        setDefaultText(view, R.id.endLocationTextInput, options.end)
//    }

    private fun setDefaultText(view: View, editTextId: Int, text: String?) {
        if (text != null) {
            val textInput = view.findViewById<EditText>(editTextId)
            textInput?.setText(text)
        }
    }

//    private fun getCurrentLocation(): AlertDialog? {
//        return activity?.let {
//
//            val inflater = requireActivity().layoutInflater
//            val view = inflater.inflate(R.layout.choose_directions, null)
//
////            setDefaultLocations(view)
//
//            val builder = AlertDialog.Builder(it)
//                .setView(view)
//                .setMessage("Enter start and end location")
//                .setPositiveButton("Go") { _, _ ->
//                    val startEditText =
//                        dialog?.findViewById<EditText>(R.id.startLocationTextInput)
//                    val endEditText = dialog?.findViewById<EditText>(R.id.endLocationTextInput)
//
//                    val start = startEditText?.text.toString()
//                    val end = endEditText?.text.toString()
//
//                    options.confirmationListener.onConfirm(start, end)
//                }
//                .setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.cancel()
//                }
//
//            builder.create()
//
//            val getDirectionsDialogFragment =
//                GetDirectionsDialogFragment(
//                    GetDirectionsDialogFragment.DirectionsDialogOptions(
//                        null, null,
//                        EmptyDirectionsGuard(this,
//                            CallbackDirectionsConfirmListener { start, end ->
//                                //Display the directions time
//                                route.set(start, end)
//                            })
//                    )
//                )
//            getDirectionsDialogFragment.show(supportFragmentManager, "directionsDialog")
//
//            return builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//
//
////        this.activity?.let {
////            mFusedLocationProviderClient.lastLocation.addOnSuccessListener(it) { location ->
////                    val currentLatLng = LatLng(location.latitude, location.longitude)
////                Log.d("TEST", "current Location: $currentLatLng")
////            }
////        }
//    }

//    private fun calculateDirections(marker : Marker) {
//        Log.d("TEST", "calculateDirections: calculating directions.");
//
//        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
//            if (location != null) {
////                val destination : com.google.maps.model.LatLng = com.google.maps.model.LatLng(
////                    destinationMarker.position.latitude,
////                    destinationMarker.position.latitude
////                )
//
//                val destination : com.google.maps.model.LatLng = com.google.maps.model.LatLng(
//                    location.latitude,
//                    location.longitude
//                )
//
//                //Current user location
//                val currentLatLng = LatLng(location.latitude, location.longitude)
//
//                val directions = DirectionsApiRequest(mGeoApiContext)
//                directions.alternatives(true)
//                directions.origin(
//                    com.google.maps.model.LatLng(
//                        currentLatLng.latitude,
//                        currentLatLng.longitude
//                    )
//                )
//                Log.d("TEST", "calculateDirections: destination: $destination")
//                directions.destination(destination)
//                    .setCallback(object : PendingResult.Callback<DirectionsResult> {
//                        override fun onResult(result: DirectionsResult) {
//                            Log.d("TEST", "calculateDirections: routes: " + result.routes[0].toString());
//                            Log.d("TEST", "calculateDirections: geocodeWayPoints : " + result.geocodedWaypoints[0].toString());
//                        }
//
//                        override fun onFailure(e: Throwable) {
//                            Log.e("TEST", "onFailure: " + e.message)
//                        }
//                    })
//            }
//        }
//    }
}