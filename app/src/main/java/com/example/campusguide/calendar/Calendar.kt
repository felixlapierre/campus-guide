package com.example.campusguide.calendar

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.CalendarContract
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import android.database.Cursor
import com.example.campusguide.R
import com.google.android.material.navigation.NavigationView

/**
 * Class for handling the user's calendars.
 * Performs all queries and saves information related to calendars and events.
 */
class Calendar constructor (val activity: MapsActivity, private val userEmail: String) {

    private var calendarsList: ArrayList<Pair<Long, String>> = arrayListOf()
    private val VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Calendars._ID, // long type
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // string type
        CalendarContract.Calendars.OWNER_ACCOUNT
    )
    private lateinit var selectedCalendar: Pair<Long, String>

    private val contentResolver = activity.applicationContext.contentResolver
    private val uri: Uri = CalendarContract.Calendars.CONTENT_URI

    // query to get calendars
    @SuppressLint("MissingPermission")
    fun getCalendars(): ArrayList<Pair<Long, String>> {
        val selection: String = "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND (" +
                "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?))"

        val selectionArgs: Array<String> = arrayOf(userEmail, "com.google")

        val cur: Cursor? = contentResolver.query(
            uri, VALUES_TO_QUERY, selection, selectionArgs, null)

        // save calendar names and IDs
        if (cur != null) {
            while (cur.moveToNext()) {
                val calID: Long = cur.getLong(Constants.PROJECTION_ID_INDEX)
                val calDisplayName: String =
                    cur.getString(Constants.PROJECTION_DISPLAY_NAME_INDEX)
                val calDataPair = Pair(calID, calDisplayName)
                calendarsList.add(calDataPair)
            }
        }
        return calendarsList
    }

    fun setSelectedCalendar(calName: String) {
        // find ID for selected calendar
        for(pair in calendarsList) {
            if (pair.second == calName) {
                selectedCalendar = pair
            }
        }
    }

    // TODO: check in with felix('s code)
    private fun parseLocation(location: String): String {
        val parsedLocation = ""

        return parsedLocation
    }

    fun setCalendarMenuItemName(calName: String){
        val navView = activity.findViewById<NavigationView>(R.id.nav_view)
        navView.menu.findItem(R.id.calendar).title = "Calendar: $calName"
    }
}