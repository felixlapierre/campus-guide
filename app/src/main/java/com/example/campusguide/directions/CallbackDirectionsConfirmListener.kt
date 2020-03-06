package com.example.campusguide.directions

class CallbackDirectionsConfirmListener constructor(private val callback: (String, String, String) -> Unit)
    : DirectionsDialogConfirmationListener {

    override fun onConfirm(start: String, end: String, travelMode: String) {
        callback(start, end, travelMode)
    }
}