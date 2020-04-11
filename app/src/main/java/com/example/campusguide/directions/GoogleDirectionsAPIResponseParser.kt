package com.example.campusguide.directions

import org.json.JSONObject

interface GoogleDirectionsAPIResponseParser {
    fun parse(response: JSONObject): GoogleDirectionsAPIResponse?
}
