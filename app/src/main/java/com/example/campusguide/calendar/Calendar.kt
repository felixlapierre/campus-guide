package com.example.campusguide.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.content.ContentResolver
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import android.database.Cursor


class Calendar constructor (activity: MapsActivity){
    private var calendars_list: ArrayList<Pair<Long, String>> = arrayListOf<Pair<Long, String>>()
    private var selected_calendar_id: Long = 0 // can't lateinit
    private val CALENDAR_VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        CalendarContract.Calendars.OWNER_ACCOUNT
    )
    private val  contentResolver = activity.applicationContext.contentResolver
    private val uri: Uri = CalendarContract.Calendars.CONTENT_URI

    // query to get calendars
    @SuppressLint("MissingPermission") // TODO: crying
    private fun getCalendars() {
        val cur: Cursor? = contentResolver.query(
            uri, CALENDAR_VALUES_TO_QUERY, null, null, null)

        // save calendar names and IDs
        if (cur != null) {
            while(cur.moveToNext()) {
                val calID: Long = cur.getLong(Constants.PROJECTION_ID_INDEX)
                val calDisplayName: String = cur.getString(Constants.PROJECTION_DISPLAY_NAME_INDEX)
                val calDataPair = Pair(calID, calDisplayName)
                calendars_list.add(calDataPair)
            }
        }
    }

    // TODO: not sure if keep private for here on out
    //  (need to set listeners and shit???)
    private fun displayCalendarNames(){
        for(pair in calendars_list){
            // display pair.second (the name) for selection
        }
    }

    private fun selectCal(calName: String){
        // find ID for selected calendar
        for(pair in calendars_list){
            if (pair.second == calName)
                selected_calendar_id = pair.first
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