package com.example.campusguide.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.example.campusguide.R

//, private val calendars: ArrayList<Pair<Long, String>>
class ChooseCalendar constructor(private val calendarsList: ArrayList<Pair<Long, String>>)
    : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.calendar_selection_list, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val radioGroup = view.findViewById<RadioGroup>(R.id.calendarGroup)

        // TODO: Prevent duplication of list each time Calendars button is clicked
        for(i in calendarsList) {
            val newCal = RadioButton(activity?.applicationContext)
            newCal.text = i.second
            radioGroup.addView(newCal)
        }

        return builder.create()
    }

}