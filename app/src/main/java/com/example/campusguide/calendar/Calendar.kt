package com.example.campusguide.calendar

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.CalendarContract
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import android.database.Cursor


class Calendar constructor (activity: MapsActivity){
    private var calendarsList: ArrayList<Pair<Long, String>> = arrayListOf()
    private lateinit var selectedCalendar: Pair<Long, String>
    private val VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Calendars._ID, // long type
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // string type
        CalendarContract.Calendars.OWNER_ACCOUNT
    )
    private val  contentResolver = activity.applicationContext.contentResolver
    private val uri: Uri = CalendarContract.Calendars.CONTENT_URI
    private val email: String = activity.getUserEmail()

    // query to get calendars
    @SuppressLint("MissingPermission") // TODO: crying
    private fun getCalendars() {
        val selection: String = "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND (" +
                "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?) AND (" +
                "${CalendarContract.Calendars.OWNER_ACCOUNT} = ?))"

        val selectionArgs: Array<String> = arrayOf(email, "com.example", email)

        val cur: Cursor? = contentResolver.query(
            uri, VALUES_TO_QUERY, selection, selectionArgs, null)

        // save calendar names and IDs
        if (cur != null) {
            while(cur.moveToNext()) {
                val calID: Long = cur.getLong(Constants.PROJECTION_ID_INDEX)
                val calDisplayName: String = cur.getString(Constants.PROJECTION_DISPLAY_NAME_INDEX)
                val calDataPair = Pair(calID, calDisplayName)
                calendarsList.add(calDataPair)
            }
        }
    }

    // TODO: not sure if keep private for here on out
    //  (need to set listeners and shit???)
    private fun displayCalendarNames(){
        for(pair in calendarsList){
            // display pair.second (the name) for selection
        }
    }

    private fun selectCal(calName: String){
        // find ID for selected calendar
        for(pair in calendarsList){
            if (pair.second == calName)
                selectedCalendar = pair
        }
    }

    // TODO: autocomplete destination
    private fun getNextEventLocation() {
        // get current time

        // get next event based on current time

        // keep location specifically

        // autocomplete destination

        // if no event fount --> do SOMETHING
    }

    private fun getLastEventLocation() {
        // get current time

        // get last event based on current time
        // -> current

        // keep location specifically

        // autocomplete destination

        // if no event fount --> do SOMETHING
    }

    // TODO: check in with felix('s code)
    private fun parseLocation(location: String): String {
        val parsedLocation = ""

        return parsedLocation
    }

}