package com.example.campusguide.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.campusguide.MapsActivity

import com.example.campusguide.R
import com.example.campusguide.utils.DisplayMessageErrorListener

//, private val calendars: ArrayList<Pair<Long, String>>
class ChooseCalendar constructor(activity: MapsActivity) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.calendar_selection_list, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val chooseCalendarLayout = view.findViewById<LinearLayout>(R.id.chooseCalendarLayout)
        val radioGroup = view.findViewById<RadioGroup>(R.id.calendarGroup)

        for(i in 1..4) {
            val newCal = RadioButton(activity?.applicationContext)
            newCal.text = i.toString()
            radioGroup.addView(newCal)
        }

        return builder.create()
    }

}