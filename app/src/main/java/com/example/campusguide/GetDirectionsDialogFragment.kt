package com.example.campusguide

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class GetDirectionsDialogFragment constructor(options: DirectionsDialogOptions) : DialogFragment() {
    private val options: DirectionsDialogOptions = options

    class DirectionsDialogOptions constructor(start: String?, end: String?, onConfirm: (String, String) -> Unit) {
        val defaultStart = start
        val defaultEnd = end
        val onConfirm = onConfirm
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;

            builder.setView(inflater.inflate(R.layout.directions_dialog_layout, null))
                .setMessage("Enter start and end location")
                .setPositiveButton(
                    "Go"
                ) { dialog, id ->
                    val startEditText = getDialog()?.findViewById<EditText>(R.id.startLocationTextInput)
                    val endEditText = getDialog()?.findViewById<EditText>(R.id.endLocationTextInput)

                    val start = startEditText?.text.toString()
                    val end = endEditText?.text.toString()
                    options.onConfirm(start, end)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, id ->
                    getDialog()?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}