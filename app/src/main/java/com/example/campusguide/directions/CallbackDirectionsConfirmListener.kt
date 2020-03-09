package com.example.campusguide.directions

class CallbackDirectionsConfirmListener constructor(private val callback: (String, String) -> Unit)
    : DirectionsDialogConfirmationListener {

    override fun onConfirm(start: String, end: String) {
        callback(start, end)
    }
}