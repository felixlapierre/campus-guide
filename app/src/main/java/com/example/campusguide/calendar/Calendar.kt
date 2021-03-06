package com.example.campusguide.calendar

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.google.android.material.navigation.NavigationView
import database.ObjectBox
import database.entity.Calendar
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlin.collections.ArrayList

/**
 * Class for handling the user's calendars.
 * Performs all queries and saves information related to calendars and events.
 */
class Calendar constructor(val activity: MapsActivity, userEmail: String) {

    private val CALENDAR_VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        CalendarContract.Calendars.OWNER_ACCOUNT
    )
    private val PROJECTION_ID_INDEX: Int = 0
    private val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
    private val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

    private val contentResolver = activity.applicationContext.contentResolver
    private val calendarUri: Uri = CalendarContract.Calendars.CONTENT_URI

    private val email: String = userEmail
    private var calendarsList: ArrayList<Pair<Long, String>> = arrayListOf()

    // permissions checked elsewhere - suppressing to get rid of IDE's complaints
    @SuppressLint("MissingPermission")
    fun getCalendars(): ArrayList<Pair<Long, String>> {
        val selection: String = "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND (" +
                "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?))"

        val selectionArgs: Array<String> = arrayOf(email, "com.google")

        // cursor to iterate through query's response
        val cur: Cursor? = contentResolver.query(
            calendarUri, CALENDAR_VALUES_TO_QUERY, selection, selectionArgs, null
        )

        // save calendar names and IDs
        setSortedCalendarsList(cur)

        return calendarsList
    }

    private fun setSortedCalendarsList(cur: Cursor?) {
        var sortedList =
            getUnsortedCalendarsList(cur).sortedWith(
                compareBy { it.second.toLowerCase() })

        // sorted list is List type -> put elements in our arrayList through iteration
        for (pair in sortedList) {
            calendarsList.add(pair)
        }
    }

    private fun getUnsortedCalendarsList(cur: Cursor?): ArrayList<Pair<Long, String>> {
        var calendars = arrayListOf<Pair<Long, String>>()
        if (cur != null) {
            while (cur.moveToNext()) {
                val calID: Long = cur.getLong(PROJECTION_ID_INDEX)
                val calDisplayName: String =
                    cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
                val calDataPair = Pair(calID, calDisplayName)
                calendars.add(calDataPair)
            }
        }
        return calendars
    }

    fun setSelectedCalendar(calName: String) {
        // find ID for selected calendar
        for (pair in calendarsList) {
            if (pair.second == calName) {
                putCalendarInDB(pair)
            }
        }
    }

    fun unsetCalendar() {
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)
        navView.menu.findItem(R.id.calendar).title = Constants.CHOOSE_CALENDAR
        val calendar: Box<database.entity.Calendar> = ObjectBox.boxStore.boxFor()
        calendar.removeAll()
    }

    private fun putCalendarInDB(pair: Pair<Long, String>) {
        // make sure there are no other calendars in the DB
        val calendar: Box<database.entity.Calendar> = ObjectBox.boxStore.boxFor()
        calendar.removeAll()

        val c = Calendar(id = pair.first, name = pair.second)
        calendar.put(c)
    }
}
