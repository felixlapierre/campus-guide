package com.example.campusguide.directions

import com.example.campusguide.utils.ErrorListener

class EmptyDirectionsGuard constructor(private val wrapped: DirectionsDialogConfirmationListener, private val errorListener: ErrorListener)
    : DirectionsDialogConfirmationListener {

    val errorMessage = "Start location, end location and travel mode must all not be blank."

    override fun onConfirm(start: String, end: String, travelMode: String) {
        if(start.isNotEmpty() && end.isNotEmpty() && travelMode.isNotEmpty()) {
            wrapped.onConfirm(start, end, travelMode)
        }
        else {
            errorListener.onError(errorMessage)
        }
    }
}