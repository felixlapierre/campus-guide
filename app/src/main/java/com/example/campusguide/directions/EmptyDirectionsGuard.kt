package com.example.campusguide.directions

import com.example.campusguide.utils.ErrorListener

class EmptyDirectionsGuard constructor(
    private val wrapped: DirectionsDialogConfirmationListener,
    private val errorListener: ErrorListener
) : DirectionsDialogConfirmationListener {

    val errorMessage = "Start and end location must both not be blank."

    override fun onConfirm(start: String, end: String) {
        if (start.isNotEmpty() && end.isNotEmpty()) {
            wrapped.onConfirm(start, end)
        } else {
            errorListener.onError(errorMessage)
        }
    }
}
