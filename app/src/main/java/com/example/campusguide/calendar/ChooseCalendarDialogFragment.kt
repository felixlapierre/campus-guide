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
import com.example.campusguide.R

class ChooseCalendarDialogFragment constructor(
    private val calendar: Calendar,
    private val calendarsList: ArrayList<Pair<Long, String>>
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.calendar_selection_list, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)
            .setTitle("Choose your calendar")

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val radioGroup = view.findViewById<RadioGroup>(R.id.calendarGroup)

        for(i in calendarsList) {
            val newCal = RadioButton(activity?.applicationContext)
            newCal.text = i.second
            radioGroup.addView(newCal)
        }

        builder.setPositiveButton("OK") { dialog, id ->
            val checkedId = radioGroup.checkedRadioButtonId
            val radioButton = radioGroup.findViewById<RadioButton>(checkedId)
            val selectedText = radioButton.text.toString()
            calendar.setSelectedCalendar(selectedText)
            Toast.makeText(activity, "Calendar set to: $selectedText", Toast.LENGTH_LONG).show()
            calendar.setCalendarMenuItemName(selectedText)
        }.setNegativeButton("Cancel", null)

        return builder.create()
    }
}