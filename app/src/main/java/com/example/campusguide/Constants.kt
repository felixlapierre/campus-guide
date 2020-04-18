package com.example.campusguide

object Constants {
    const val ZOOM_STREET_LVL = 17.0f
    const val ZOOM_INDOOR_LVL = 18.0f
    const val PERMISSION_REQUEST_CODE = 1
    const val REGULAR_SEARCH_REQUEST_CODE = 2
    const val ORIGIN_SEARCH_REQUEST_CODE = 3
    const val DESTINATION_SEARCH_REQUEST_CODE = 4

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

    const val CONFIRM_CHOICE = "CONFIRM"
    const val CANCEL_CHOICE = "Cancel"

    const val MAPS_ACTIVITY_CONTENT_DESCRIPTION = "maps_activity_map ready"
    const val DIRECTIONS_ACTIVITY_CONTENT_DESCRIPTION = "directions_activity_map ready"

    val SGW_COORDINATES = com.google.android.gms.maps.model.LatLng(
        45.495792, -73.578096
    )

    val LOY_COORDINATES = com.google.android.gms.maps.model.LatLng(
        45.458153, -73.640490
    )

    const val PATTERN_DASH_LENGTH_PX = 20F
    const val PATTERN_GAP_LENGTH_PX = 20F

    const val PRIMARY_COLOR = "#008577"
    const val PRIMARY_COLOR_DARK = "#00574B"
    const val ACCENT_COLOR = "#D81B60"
    const val AZURE_COLOR = "#007fff"

    const val TRAVEL_MODE_DRIVING = "driving"
    const val TRAVEL_MODE_WALKING = "walking"
    const val TRAVEL_MODE_TRANSIT = "transit"
    const val TITLE_RECOMMENDED_ROUTE = "RECOMMENDED_ROUTE"
    const val TITLE_LESS_WALKING = "LESS WALKING"
    const val TITLE_FEWER_TRANSFERS = "FEWER TRANSFERS"
}
