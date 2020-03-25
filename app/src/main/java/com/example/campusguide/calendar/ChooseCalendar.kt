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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.campusguide.MapsActivity

import com.example.campusguide.R
import com.example.campusguide.utils.DisplayMessageErrorListener

class ChooseCalendar constructor(activity: MapsActivity, private val calendars: ArrayList<Pair<Long, String>>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.calendar_selection_list, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)

        builder.setView(view)
        builder.create().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val chooseCalendarLayout = view.findViewById<LinearLayout>(R.id.chooseCalendarLayout)
        val newCal = RadioButton(activity?.applicationContext)

        for(i in 1..3) {
            newCal.setText(i)
            newCal.layoutParams = layoutParams
            chooseCalendarLayout.addView(newCal)
        }

        return builder.create()
    }

    fun openChooseCalendarDialogFragment() {

    }
}