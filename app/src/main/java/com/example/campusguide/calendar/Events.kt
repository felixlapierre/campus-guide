package com.example.campusguide.calendar

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import java.util.*
import kotlin.collections.ArrayList

class Events constructor(
    val activity: MapsActivity,
    private var selectedCalendar: Pair<Long, String>
) {

    private val contentResolver = activity.applicationContext.contentResolver

    private val EVENT_VALUES_TO_QUERY: Array<String> = arrayOf(
        CalendarContract.Events.CALENDAR_ID, // long
        CalendarContract.Events.DTSTART, // start in UTC milliseconds since the epoch
        CalendarContract.Events.EVENT_LOCATION // string
    )
    private val PROJECTION_EVENT_CALENDAR_INDEX: Int = 0
    private val PROJECTION_EVENT_START_TIME_INDEX: Int = 1
    private val PROJECTION_EVENT_LOCATION_INDEX: Int = 2

    private val eventUri: Uri = CalendarContract.Events.CONTENT_URI
    private val todaysEvents: ArrayList<Pair<Long, String>> =
        arrayListOf() // event time & location str

    // TODO: WIP for UC-16
    @SuppressLint("MissingPermission")
    private fun setTodaysEvents() {
        // get calendar's events
        val selection: String = "(${CalendarContract.Events.CALENDAR_ID} = ?)"
        val selectionArgs: Array<String> = arrayOf(selectedCalendar.first.toString())

        val cur: Cursor? = contentResolver.query(
            eventUri, EVENT_VALUES_TO_QUERY, selection, selectionArgs, null
        )

        val calendarEvents: ArrayList<Pair<Long, String>> = arrayListOf()

        if (cur != null) {
            while (cur.moveToNext()) {
                val eventTime = cur.getLong(PROJECTION_EVENT_START_TIME_INDEX)
                val eventLocation = cur.getString(PROJECTION_EVENT_LOCATION_INDEX)
                val eventPair = Pair(eventTime, eventLocation)
                calendarEvents.add(eventPair)
            }
        }

        // narrow down to today & make sure it's sorted chronologically
        val unsortedEvents: ArrayList<Pair<Long, String>> = arrayListOf()
        for (pair in calendarEvents) {
            if(pair.first > getDayStart() && pair.first < getDayEnd()){
                unsortedEvents.add(pair)
            }
        }

        val sortedList = unsortedEvents.sortedWith(compareBy { it.second.toLowerCase() })

        for (pair in sortedList){
            todaysEvents.add(pair)
        }

    }

    // TODO: WIP for UC-16, autocomplete destination
    fun getNextEventLocation(): String {

        var nextEventLocation = ""

        if (todaysEvents.isEmpty()) {
            setTodaysEvents()
        }
        // get current time
        val now = System.currentTimeMillis()

        // get next event based on current time
        for (event in todaysEvents) {
            if (event.first > now) {
                nextEventLocation = event.second
                break
            }
        }

        if (nextEventLocation == "") {
            // something
        }

        return nextEventLocation
    }

    // TODO: WIP for UC-16
    fun getLastEventLocation(): String {

        var lastEventLocation = ""

        if (todaysEvents.isEmpty()) {
            setTodaysEvents()
        }

        // get current time
        val now = System.currentTimeMillis()

        // get last event based on current time
        // -> current
        var eventsBeforeNow: ArrayList<Pair<Long, String>> = arrayListOf()
        for (event in todaysEvents) {
            if (event.first < now) {
                eventsBeforeNow.add(event)
            }
        }

        // keep location specifically
        val lastEventIndex = eventsBeforeNow.size - 1
        lastEventLocation = eventsBeforeNow[lastEventIndex].second

        if (lastEventLocation == "") {
            // something
        }

        return lastEventLocation
    }

    // TODO: WIP for UC-16, check in with Felix('s code)
    private fun parseLocation(location: String): String {
        val parsedLocation = ""

        return parsedLocation
    }

    private fun getDayStart(): Long {
        val dayStart = Date()
        dayStart.hours = 0
        dayStart.minutes = 0
        dayStart.seconds = 0

        return dayStart.time
    }

    private fun getDayEnd(): Long {
        val dayEnd = Date()
        dayEnd.hours = 23
        dayEnd.minutes = 59
        dayEnd.seconds = 59

        return dayEnd.time
    }

}