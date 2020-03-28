package com.example.campusguide

object Constants {
    const val ZOOM_STREET_LVL = 17.0f;
    const val PERMISSION_REQUEST_CODE = 1
    const val AUTOCOMPLETE_REQUEST_CODE = 2
    const val LOYOLA_CAMPUS = "Loyola Campus"
    const val SGW_CAMPUS = "SGW Campus"

    const val DIRECTIONS_API_NULL_RESPONSE = "Could not get a response from the Google Directions API"
    const val DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json"

    const val LOCATION_MARKER_TITLE = "You are here."

    const val INDOOR_LOCATION_IDENTIFIER = "indoor"

    const val SEARCH_BOTTOM_BOUND = 45.351240
    const val SEARCH_LEFT_BOUND = -74.031124
    const val SEARCH_TOP_BOUND = 45.734999
    const val SEARCH_RIGHT_BOUND = -73.417826

    const val PROJECTION_ID_INDEX: Int = 0
    const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2

    const val PROJECTION_EVENT_CALENDAR_INDEX: Int = 0
    const val PROJECTION_EVENT_START_TIME_INDEX: Int = 1
    const val PROJECTION_EVENT_LOCATION_INDEX: Int = 2
}