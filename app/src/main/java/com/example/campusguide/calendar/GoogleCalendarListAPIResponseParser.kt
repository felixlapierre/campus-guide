package com.example.campusguide.calendar

import org.json.JSONObject

interface GoogleCalendarListAPIResponseParser {
    fun parse(response: JSONObject): GoogleCalendarListAPIResponse?
}