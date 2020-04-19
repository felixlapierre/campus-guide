package com.example.campusguide.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.campusguide.Constants
import java.lang.IllegalStateException

class MessageDialogFragment constructor(private val message: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
            builder.setPositiveButton(Constants.CONFIRM_CHOICE) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException(Constants.ACTIVITY_NULL_MSG)
    }
}
