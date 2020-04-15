package com.example.campusguide.directions

import com.beust.klaxon.Json

/**
 * A set of data classes to represent the contents
 * of a Google Directions API response.
 */
data class GoogleDirectionsAPIResponse(

    @Json(name = "status")
    val status: String,

    @Json(name = "geocoded_waypoints")
    val geocodedWaypoints: List<GoogleDirectionsAPIGeocodedWaypoint>,

    @Json(name = "routes")
    val routes: List<GoogleDirectionsAPIRoute>
)

data class GoogleDirectionsAPIGeocodedWaypoint(

    @Json(name = "geocoder_status")
    val geocoderStatus: String,

    @Json(name = "place_id")
    val placeId: String,

    @Json(name = "types")
    val types: List<String>
)

data class GoogleDirectionsAPIRoute(

    @Json(name = "bounds")
    val bounds: GoogleDirectionsAPIBounds,

    @Json(name = "copyrights")
    val copyrights: String,

    @Json(name = "legs")
    val legs: List<GoogleDirectionsAPILeg>,

    @Json(name = "overview_polyline")
    val overviewPolyline: GoogleDirectionsAPIPolyline,

    @Json(name = "summary")
    val summary: String,

    @Json(name = "warnings")
    val warnings: List<Any>,

    @Json(name = "waypoint_order")
    var waypointOrder: List<Int>
)

data class GoogleDirectionsAPIBounds(

    @Json(name = "northeast")
    val northeast: GoogleDirectionsAPILocation,

    @Json(name = "southwest")
    val southwest: GoogleDirectionsAPILocation
)

data class GoogleDirectionsAPILeg(

    @Json(name = "distance")
    val distance: GoogleDirectionsAPITextValuePair,

    @Json(name = "duration")
    val duration: GoogleDirectionsAPITextValuePair,

    @Json(name = "end_address")
    val endAddress: String,

    @Json(name = "end_location")
    val endLocation: GoogleDirectionsAPILocation,

    @Json(name = "start_address")
    val startAddress: String,

    @Json(name = "start_location")
    val startLocation: GoogleDirectionsAPILocation,

    @Json(name = "steps")
    val steps: List<GoogleDirectionsAPIStep>,

    @Json(name = "traffic_speed_entry")
    val trafficSpeedEntry: List<Any>,

    @Json(name = "via_waypoint")
    val viaWaypoint: List<Any>
)

data class GoogleDirectionsAPIStep(

    @Json(name = "distance")
    val distance: GoogleDirectionsAPITextValuePair,

    @Json(name = "duration")
    val duration: GoogleDirectionsAPITextValuePair,

    @Json(name = "end_location")
    val endLocation: GoogleDirectionsAPILocation,

    @Json(name = "html_instructions")
    val htmlInstruction: String,

    @Json(name = "maneuver")
    val maneuver: String = "",

    @Json(name = "polyline")
    val polyline: GoogleDirectionsAPIPolyline,

    @Json(name = "start_location")
    val startLocation: GoogleDirectionsAPILocation,

    @Json(name = "travel_mode")
    val travelMode: String,

    @Json(name = "transit_details")
    val transitDetails: GoogleDirectionsAPITransitDetails = GoogleDirectionsAPITransitDetails()
)

data class GoogleDirectionsAPITransitDetails(

    @Json(name = "line")
    val line: GoogleDirectionsAPITransitLine = GoogleDirectionsAPITransitLine()
)

data class GoogleDirectionsAPITransitLine(

    @Json(name = "short_name")
    val shortName: String = "",

    @Json(name = "color")
    val color: String = ""
)

data class GoogleDirectionsAPILocation(

    @Json(name = "lat")
    val lat: Float,

    @Json(name = "lng")
    val lng: Float
)

data class GoogleDirectionsAPITextValuePair(

    @Json(name = "text")
    val text: String,

    @Json(name = "value")
    val value: Int
)

data class GoogleDirectionsAPIPolyline(

    @Json(name = "points")
    val points: String
)
