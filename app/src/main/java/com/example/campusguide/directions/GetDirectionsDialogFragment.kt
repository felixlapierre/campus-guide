package com.example.campusguide.directions

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R

/**
 * An Android fragment that contains a dialog window prompting the user to
 * enter their start and end location for getting directions.
 */
class GetDirectionsDialogFragment constructor(private val options: DirectionsDialogOptions) :
    DialogFragment() {

    /**
     * Options for creating a Directions dialog.
     * @param message Displays appropriate instructions to user
     * @param start Autofills the start location to the specified value
     * @param end Autofills the end location to the specified value
     * @param onConfirm Callback that will be executed when the user confirms the dialog window
     */
    class DirectionsDialogOptions constructor(
        val message: String?,
        val start: String?,
        val end: String?,
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

            setDefaultLocations(view)

            val builder = AlertDialog.Builder(it)
                .setView(view)
                .setMessage(this.options.message)
                .setPositiveButton("Go") { _, _ ->
                    val startEditText =
                        dialog?.findViewById<EditText>(R.id.startLocationTextInput)
                    val endEditText = dialog?.findViewById<EditText>(R.id.endLocationTextInput)

                    val start = startEditText?.text.toString()
                    val end = endEditText?.text.toString()

                    options.confirmationListener.onConfirm(start, end)
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
