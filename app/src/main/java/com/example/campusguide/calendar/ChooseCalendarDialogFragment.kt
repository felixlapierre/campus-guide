package com.example.campusguide.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.utils.CalendarUtils

/**
 * An Android fragment that contains a dialog window prompting the user to
 * select the calendar they would like to use for event location parsing.
 */

class ChooseCalendarDialogFragment constructor(
    private val activity: MapsActivity,
    private val calendar: Calendar,
    private val calendarsList: ArrayList<Pair<Long, String>>
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.calendar_selection_list, null)

        // Set title of dialog box
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)
            .setTitle("Choose your calendar")

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val radioGroup = view.findViewById<RadioGroup>(R.id.calendarGroup)

        fillRadioGroup(radioGroup)

        builder.setPositiveButton("OK") { _, _ ->
            handleOkSelected(radioGroup)
        }.setNegativeButton("Cancel", null)

        return builder.create()
    }

    private fun fillRadioGroup(radioGroup: RadioGroup) {
        // Create radio button options for each calendar found on the logged in account
        for(pair in calendarsList) {
            val newCal = RadioButton(activity?.applicationContext)
            newCal.text = pair.second
            radioGroup.addView(newCal)
        }
    }

    private fun handleOkSelected(radioGroup: RadioGroup) {
        // Find which calendar was selected
        val checkedId = radioGroup.checkedRadioButtonId
        if (checkedId != -1) {
            val radioButton = radioGroup.findViewById<RadioButton>(checkedId)
            val selectedText = radioButton.text.toString()
            // Return selected calendar
            calendar.setSelectedCalendar(selectedText)
            Toast.makeText(activity, "Calendar set to: $selectedText", Toast.LENGTH_LONG).show()
            // Change menu item title for Calendar to include selected calendar
            val calendarUtils = CalendarUtils(activity)
            calendarUtils.setCalendarMenuItemName(selectedText)
        }
    }
}
