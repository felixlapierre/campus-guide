package com.example.campusguide.calendar

import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import kotlin.collections.ArrayList
import com.example.campusguide.R
import com.google.android.material.navigation.NavigationView

/**
 * Class for handling the user's calendars.
 * Performs all queries and saves information related to calendars and events.
 */

class Calendar constructor (val activity: MapsActivity, private val userEmail: String) {

    private val  contentResolver = activity.applicationContext.contentResolver

    // calendar variables --------------------------------------------------------------------------
    private var calendarsList: ArrayList<Pair<Long, String>> = arrayListOf()
    private lateinit var selectedCalendar: Pair<Long, String> // calendar ID and name str
    private val CALENDAR_VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Calendars._ID, // long type
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // string type
        CalendarContract.Calendars.OWNER_ACCOUNT
    )

    private val calendarUri: Uri = CalendarContract.Calendars.CONTENT_URI
    private val email: String = userEmail

    // event variables -----------------------------------------------------------------------------
    private val EVENT_VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Events.CALENDAR_ID, // long
        CalendarContract.Events.DTSTART, // start in UTC milliseconds since the epoch
        CalendarContract.Events.EVENT_LOCATION // string
    )
    private val eventUri: Uri = CalendarContract.Events.CONTENT_URI
    private val todaysEvents: ArrayList<Pair<Long, String>> = arrayListOf() // event time & location str

    // METHODS -------------------------------------------------------------------------------------
    // TODO: permissions
    fun getCalendars():ArrayList<Pair<Long, String>> {
        val selection: String = "((${CalendarContract.Calendars.ACCOUNT_NAME} = ?) AND (" +
                "${CalendarContract.Calendars.ACCOUNT_TYPE} = ?))"

        val selectionArgs: Array<String> = arrayOf(email, "com.google")

        val cur: Cursor? = contentResolver.query(
            calendarUri, CALENDAR_VALUES_TO_QUERY, selection, selectionArgs, null)

        // save calendar names and IDs
        var calendars = arrayListOf<Pair<Long, String>>()
        if (cur != null) {
            while (cur.moveToNext()) {
                val calID: Long = cur.getLong(Constants.PROJECTION_ID_INDEX)
                val calDisplayName: String =
                    cur.getString(Constants.PROJECTION_DISPLAY_NAME_INDEX)
                val calDataPair = Pair(calID, calDisplayName)
                calendars.add(calDataPair)
            }
        }

        var sortedList = calendars.sortedWith(compareBy {it.second.toLowerCase()})

        for(pair in sortedList){
            calendarsList.add(pair)
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

    private fun setTodaysEvents() {
        // get calendar's events
        val selection: String = "(${CalendarContract.Events.CALENDAR_ID} = ?)"
        val selectionArgs: Array<String> = arrayOf(selectedCalendar.first.toString())

        val cur: Cursor? = contentResolver.query(
            eventUri, EVENT_VALUES_TO_QUERY, selection, selectionArgs, null)

        val calendarEvents: ArrayList<Pair<Long, String>> = arrayListOf()

        if(cur != null) {
            while(cur.moveToNext()){
                val eventTime = cur.getLong(Constants.PROJECTION_EVENT_START_TIME_INDEX)
                val eventLocation = cur.getString(Constants.PROJECTION_EVENT_LOCATION_INDEX)
                val eventPair = Pair(eventTime, eventLocation)
                calendarEvents.add(eventPair)
            }
        }

        // narrow down to today & make sure it's sorted chronologically
        val now = System.currentTimeMillis()
        for(pair in calendarEvents){

        }

    }

    // TODO: autocomplete destination
    private fun getNextEventLocation() {

        var nextEventLocation = ""

        if(todaysEvents.isEmpty()){
            setTodaysEvents()
        } else {
            // get current time
            val now = System.currentTimeMillis()

            // get next event based on current time
            for(event in todaysEvents){
                if(event.first > now){
                    nextEventLocation = event.second
                    break
                }
            }

            if(nextEventLocation == ""){
                // something
            }

            // autocomplete destination
        }
    }

    private fun getLastEventLocation() {

        var lastEventLocation = ""

        if(todaysEvents.isEmpty()){
            setTodaysEvents()
        } else {
            // get current time
            val now = System.currentTimeMillis()

            // get last event based on current time
            // -> current
            var eventsBeforeNow: ArrayList<Pair<Long, String>> = arrayListOf()
            for(event in todaysEvents){
                if(event.first < now){
                    eventsBeforeNow.add(event)
                }
            }

            // keep location specifically
            lastEventLocation = eventsBeforeNow[-1].second

            if(lastEventLocation == ""){
                // something
            }
            // autocomplete destination

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