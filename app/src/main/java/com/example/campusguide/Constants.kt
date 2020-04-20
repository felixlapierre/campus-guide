package com.example.campusguide

object Constants {

    // Zoom levels
    const val ZOOM_STREET_LVL = 17.0f
    const val ZOOM_INDOOR_LVL = 18.0f

    // Permission codes
    const val PERMISSION_REQUEST_CODE = 1
    const val REGULAR_SEARCH_REQUEST_CODE = 2
    const val ORIGIN_SEARCH_REQUEST_CODE = 3
    const val DESTINATION_SEARCH_REQUEST_CODE = 4

    // Sign-in codes
    const val RC_SIGN_IN = 1
    const val TAG = "MapsActivity"

    // API URLs
    const val DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json"

    // Campus names
    const val LOYOLA_CAMPUS = "Loyola Campus"
    const val SGW_CAMPUS = "SGW Campus"

    // Buildings and their codes
    const val HALL = "hall"
    const val HALL_CODE = "h"
    const val LIBRARY = "library"
    const val LIBRARY_CODE = "lb"

    // Amenities identifiers
    const val INDOOR_LOCATION_IDENTIFIER = "indoor"
    const val AMENITIES_LOCATION_IDENTIFIER = "amenities"

    // Bathroom amenities
    const val AMENITIES_BATHROOM = "bathroom"
    const val AMENITIES_WOMENS_BATHROOM = "women's bathroom"
    const val AMENITIES_MENS_BATHROOM = "men's bathroom"
    const val AMENITIES_GENDER_NEUTRAL_BATHROOM = "gender neutral bathroom"

    // POI types
    const val CAFE_POI = "Cafe"
    const val RESTAURANT_POI = "Restaurant"
    const val SHOPPING_MALL_POI = "Shopping mall"
    const val PHARMACY_POI = "Pharmacy"

    // Search bounds
    const val SEARCH_BOTTOM_BOUND = 45.351240
    const val SEARCH_LEFT_BOUND = -74.031124
    const val SEARCH_TOP_BOUND = 45.734999
    const val SEARCH_RIGHT_BOUND = -73.417826

    // Dialog choices
    const val OK_CHOICE = "OK"
    const val GO_CHOICE = "Go"
    const val CONFIRM_CHOICE = "CONFIRM"
    const val CANCEL_CHOICE = "Cancel"

    // Content descriptions
    const val MAPS_ACTIVITY_CONTENT_DESCRIPTION = "maps_activity_map ready"
    const val DIRECTIONS_ACTIVITY_CONTENT_DESCRIPTION = "directions_activity_map ready"

    // Campus coordinates
    val SGW_COORDINATES = com.google.android.gms.maps.model.LatLng(
        45.495792, -73.578096
    )
    val LOY_COORDINATES = com.google.android.gms.maps.model.LatLng(
        45.458153, -73.640490
    )

    // Polyline pattern units
    const val PATTERN_DASH_LENGTH_PX = 20.0F
    const val PATTERN_GAP_LENGTH_PX = 20.0F

    // Map marker snippets
    const val LOCATION_MARKER_TITLE = "You are here."
    const val START = "Start"
    const val DESTINATION = "Destination"

    // App colors
    const val PRIMARY_COLOR = "#008577"
    const val PRIMARY_COLOR_DARK = "#00574B"
    const val ACCENT_COLOR = "#D81B60"
    const val AZURE_COLOR = "#007fff"

    // Travel modes
    const val TRAVEL_MODE_DRIVING = "driving"
    const val TRAVEL_MODE_WALKING = "walking"
    const val TRAVEL_MODE_TRANSIT = "transit"
    const val TITLE_RECOMMENDED_ROUTE = "RECOMMENDED_ROUTE"
    const val TITLE_LESS_WALKING = "LESS WALKING"
    const val TITLE_FEWER_TRANSFERS = "FEWER TRANSFERS"

    // Login menu item titles
    const val LOGIN_TO_ACCOUNT = "Login to an account"
    const val LOGOUT_OF = "Logout of"

    // Toast message
    const val LOGGED_INTO_TOAST = "Logged into"
    const val LOGGED_OUT_TOAST = "Logged out"
    const val CALENDAR_SET_TOAST = "Calendar set to:"

    // Calendar menu item titles
    const val CHOOSE_CALENDAR = "Choose your calendar"
    const val CALENDAR_SET = "Calendar:"

    // Throwable exception message for activity null and request denied
    const val ACTIVITY_NULL_MSG = "Activity cannot be null."
    const val REQUEST_DENIED_MSG = "Request to Google Directions API was denied. Make sure the" +
        " API key has permissions to access the Google Directions API."

    // Throwable exception messages for calendar and events
    const val NO_CALENDAR_LOGIN_EXCEPTION_MSG =
        "You are not logged in or you do not have a calendar set.\n" +
        "\nPlease login and choose a calendar in the drawer menu."
    const val NO_EVENTS_TODAY_MSG = "Could not find any events today."
    const val NO_EVENT_LOCATION_FOUND_MSG = "Could not find the event location."

    // Throwable exception messages for pathfinding
    const val DIRECTIONS_API_NULL_RESPONSE =
        "Could not get a response from the Google Directions API."
    const val NO_PATH_TO_ROOM = "Could not find a path to room:"
    const val LOCATION_NOT_FOUND_GRAPH = "location was not found in the graph."
    const val LOCATION_NOT_FOUND_ROOM = "Could not find room:"
    const val INDOOR_SEGMENT_NOT_FOUND_BUILDING = "not found. Cannot create IndoorSegment."

    // Throwable exception messages for IndoorLocationFinder
    const val INDOOR_IDENTIFIER_BAD_FORMAT =
        "ID is an indoor identifier that does not have the format indoor_buildingcode_roomcode."
    const val INDOOR_INDEX_NOT_LOADED = "Building index is not loaded yet."
    const val BUILDING_CODE_NOT_FOUND =
        "building code does not correspond to a building in the index."
    const val ROOM_CODE_NOT_FOUND = "room code was not found in the index."
}
