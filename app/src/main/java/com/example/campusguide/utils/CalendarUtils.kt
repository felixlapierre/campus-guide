package com.example.campusguide.utils

import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.google.android.material.navigation.NavigationView
import database.ObjectBox
import database.entity.Calendar
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class CalendarUtils constructor(private val activity: MapsActivity) {

    fun setCalendarMenuItemName(calendarName: String) {
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)
        navView.menu.findItem(R.id.calendar).title = "${Constants.CALENDAR_SET} $calendarName"
    }

    fun getCalendarNameFromDB(): String {
        val calendarBox: Box<Calendar> = ObjectBox.boxStore.boxFor()
        lateinit var calName: String
        calName = if (!calendarBox.isEmpty) {
            calendarBox.all[0].name
        } else {
            ""
        }
        return calName
    }
}
