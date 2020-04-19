package com.example.campusguide.directions

import com.beust.klaxon.Klaxon
import org.json.JSONObject

class KlaxonDirectionsAPIResponseParser : GoogleDirectionsAPIResponseParser {
    override fun parse(response: JSONObject): GoogleDirectionsAPIResponse? {
        return Klaxon().parse<GoogleDirectionsAPIResponse>(response.toString(0))
    }
}
