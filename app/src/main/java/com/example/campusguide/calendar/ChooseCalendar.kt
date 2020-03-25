package com.example.campusguide.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

import com.example.campusguide.R
import com.example.campusguide.utils.DisplayMessageErrorListener

class ChooseCalendar constructor(private val calendars: ArrayList<Pair<Long, String>>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}