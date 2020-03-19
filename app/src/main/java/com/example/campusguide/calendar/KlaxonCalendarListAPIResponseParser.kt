package com.example.campusguide.calendar

import com.beust.klaxon.Klaxon
import org.json.JSONObject

class KlaxonCalendarListAPIResponseParser: GoogleCalendarListAPIResponseParser {
    override fun parse(response: JSONObject): GoogleCalendarListAPIResponse? {
        return Klaxon().parse<GoogleCalendarListAPIResponse>(response.toString(0))
    }
}