package com.example.campusguide.calendar

import com.beust.klaxon.*

/**
 * Set of data classes to represent the contents of
 * a Google Calendar API response
 */
data class GoogleCalendarAPIResponse (
    @Json(name = "calendarID")
    val calendarID: String
)