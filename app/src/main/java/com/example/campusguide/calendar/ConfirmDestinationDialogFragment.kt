package com.example.campusguide.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.campusguide.Constants
import com.example.campusguide.R

/**
 * Android fragment that contains a dialog window prompting
 * confirmation of the user's next location to use as
 * destination for their route.
 */
class ConfirmDestinationDialogFragment constructor(
    private val events: Events
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.calendar_selection_list, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setMessage(view)

        builder.setPositiveButton(Constants.CONFIRM_CHOICE) {dialog, id ->
            Toast.makeText(activity, "Confirmed", Toast.LENGTH_LONG).show()
        }.setNegativeButton(Constants.CANCEL_CHOICE, null)

        return builder.create()
    }

    private fun setMessage(view: View){
        val location = events.getNextEventLocation()

        val layout = view.findViewById<LinearLayout>(R.id.confirmLocationLayout)

        val confirmText = TextView(activity?.applicationContext)
        confirmText.text = "Are you going to $location?"

        layout.addView(confirmText)
    }

}