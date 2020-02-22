package com.example.campusguide.directions

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.utils.MessageDialogFragment

class EmptyDirectionsGuard constructor(private val activity: AppCompatActivity, private val wrapped: GetDirectionsDialogFragment.DirectionsDialogConfirmationListener)
    : GetDirectionsDialogFragment.DirectionsDialogConfirmationListener {

    override fun onConfirm(start: String, end: String) {
        if(start.isNotEmpty() && end.isNotEmpty()) {
            wrapped.onConfirm(start, end)
        }
        else {
            val message = MessageDialogFragment("Start and end location must both not be blank")
            message.show(activity.supportFragmentManager, "message")
        }
    }
}