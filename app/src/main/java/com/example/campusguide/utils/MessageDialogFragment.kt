package com.example.campusguide.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class MessageDialogFragment constructor(private val message: String): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
            builder.setPositiveButton("Ok") { _, _ ->}
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}