package com.example.campusguide.calendar

import org.json.JSONObject

interface GoogleCalendarAPIResponseParser {
    fun parse(response: JSONObject): GoogleCalendarAPIResponse?
}