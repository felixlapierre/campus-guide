package com.example.campusguide.calendar

import com.beust.klaxon.Klaxon
import org.json.JSONObject

class KlaxonCalendarAPIResponseParser: GoogleCalendarAPIResponseParser {
    override fun parse(response: JSONObject): GoogleCalendarAPIResponse? {
        return Klaxon().parse<GoogleCalendarAPIResponse>(response.toString(0))
    }
}