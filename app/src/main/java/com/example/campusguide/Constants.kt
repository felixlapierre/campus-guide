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

    const val PROJECTION_ID_INDEX: Int = 0
    const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
    const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
    const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

    const val PROJECTION_EVENT_CALENDAR_INDEX: Int = 0
    const val PROJECTION_EVENT_START_TIME_INDEX: Int = 1
    const val PROJECTION_EVENT_LOCATION_INDEX: Int = 2
}