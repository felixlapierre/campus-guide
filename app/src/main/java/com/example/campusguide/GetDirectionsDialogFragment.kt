package com.example.campusguide

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class GetDirectionsDialogFragment constructor(private val options: DirectionsDialogOptions) :
    DialogFragment() {

    class DirectionsDialogOptions constructor(
        val start: String?, val end: String?,
        val onConfirm: (String, String) -> Unit
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.directions_dialog_layout, null)
            setDefaultLocations(view)
            builder.setView(view)
                .setMessage("Enter start and end location")
                .setPositiveButton("Go") { _, _ ->
                    val startEditText =
                        dialog?.findViewById<EditText>(R.id.startLocationTextInput)
                    val endEditText = dialog?.findViewById<EditText>(R.id.endLocationTextInput)

                    val start = startEditText?.text.toString()
                    val end = endEditText?.text.toString()
                    options.onConfirm(start, end)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setDefaultLocations(view: View) {
        setDefaultText(view, R.id.startLocationTextInput, options.start)
        setDefaultText(view, R.id.endLocationTextInput, options.end)
    }

    private fun setDefaultText(view: View, editTextId: Int, text: String?) {
        if (text != null) {
            val textInput = view.findViewById<EditText>(editTextId)
            textInput?.setText(text)
        }
    }
}