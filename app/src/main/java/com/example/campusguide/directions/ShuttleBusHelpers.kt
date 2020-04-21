package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.Calendar

fun ShuttleBusStopSGW(): LocationMetadata {
    return LocationMetadata(
        name = "Henry F. Hall Building front doors, 1455 De Maisonneuve Blvd. W., SGW Campus",
        encoded = "45.497163,-73.578535"
    )
}
fun ShuttleBusStopLOYOLA(): LocationMetadata {
    return LocationMetadata(
        name = "Loyola Chapel, 7137 Sherbrooke St. W., Loyola Campus",
        encoded = "45.458424,-73.638369"
    )
}

fun DOWNTOWN_CAMPUS_BOUNDS(): LatLngBounds {
    val builder = LatLngBounds.builder()
    builder.include(LatLng(45.496789, -73.573031)).include(LatLng(45.491396, -73.587632))
    return builder.build()
}

fun LOYOLA_CAMPUS_BOUNDS(): LatLngBounds {
    val builder = LatLngBounds.builder()
    builder.include(LatLng(45.454185, -73.650552)).include(LatLng(45.464239, -73.632860))
    return builder.build()
}

enum class Campus {
    OFF_CAMPUS,
    DOWNTOWN,
    LOYOLA
}

fun getShuttleBusGoogleAPIStepRisky(pathPolyline: PathPolyline): GoogleDirectionsAPIStep {

    return GoogleDirectionsAPIStep(
        distance = GoogleDirectionsAPITextValuePair(pathPolyline.getDistance(), 0),
        duration = GoogleDirectionsAPITextValuePair("0", pathPolyline.getDuration()),
        endLocation = GoogleDirectionsAPILocation(
            pathPolyline.getEndLocationRisky().latitude.toFloat(),
            pathPolyline.getEndLocationRisky().longitude.toFloat()),
        htmlInstruction = "Ride the <b>Concordia Shuttle Bus</b> to the other campus",
        maneuver = "shuttle",
        polyline = GoogleDirectionsAPIPolyline(""),
        startLocation = GoogleDirectionsAPILocation(
            pathPolyline.getStartLocationRisky().latitude.toFloat(),
            pathPolyline.getStartLocationRisky().longitude.toFloat()),
        transitDetails = GoogleDirectionsAPITransitDetails(
            GoogleDirectionsAPITransitLine()
        ),
        travelMode = "shuttle"
    )
}

suspend fun getShuttleBusGoogleAPIStep(pathPolyline: PathPolyline): GoogleDirectionsAPIStep {

    pathPolyline.waitUntilCreated()

    return GoogleDirectionsAPIStep(
        distance = GoogleDirectionsAPITextValuePair("0", 0),
        duration = GoogleDirectionsAPITextValuePair("0", 0),
        endLocation = GoogleDirectionsAPILocation(
            pathPolyline.getPaths().first().points.first().latitude.toFloat(),
            pathPolyline.getPaths().first().points.first().longitude.toFloat()),
        htmlInstruction = "Get on the <b>Concordia Shuttle Bus</b>.",
        maneuver = "Get on Shuttle",
        polyline = GoogleDirectionsAPIPolyline(""),
        startLocation = GoogleDirectionsAPILocation(
            pathPolyline.getPaths().first().points.first().latitude.toFloat(),
            pathPolyline.getPaths().first().points.first().longitude.toFloat()),
        transitDetails = GoogleDirectionsAPITransitDetails(
            GoogleDirectionsAPITransitLine()
        ),
        travelMode = "Shuttle Bus"
    )
}

public fun shouldBeShuttleRoute(start: LocationMetadata, end: LocationMetadata): Boolean {
    val startCampus = campusFromLatLng(start.getLatLng())
    val endCampus = campusFromLatLng(end.getLatLng())
    return checkLocationsValid(startCampus, endCampus)
    // TODO: Test and integrate checkCurrTimeValid()
    // return checkCurrTimeValid() && checkLocationsValid(startCampus, endCampus)
}

fun campusFromLatLng(location: LatLng): Campus {
    return when {
        DOWNTOWN_CAMPUS_BOUNDS().contains(location) -> {
            Campus.DOWNTOWN
        }
        LOYOLA_CAMPUS_BOUNDS().contains(location) -> {
            Campus.LOYOLA
        }
        else -> {
            Campus.OFF_CAMPUS
        }
    }
}

private fun checkCurrTimeValid(calendar: Calendar = Calendar.getInstance()): Boolean {
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    // Time aware 'enough' for feature completion for W2020 semester
    // Weekends
    if (day == 1 || day == 7)
        return false
    // Fridays
    if (day == 6) {
        if (hour < 7 || (hour == 7 && minute < 40))
            return false
        if (hour > 20 || (hour == 19 && minute < 50))
            return false
    }
    // Monday to Thursday
    if (day in 2..5) {
        if (hour < 7 || (hour == 7 && minute < 30))
            return false
        if (hour > 23)
            return false
    }
    return true
}
private fun checkLocationsValid(startCampus: Campus, endCampus: Campus): Boolean {
    return startCampus != Campus.OFF_CAMPUS &&
        endCampus != Campus.OFF_CAMPUS &&
        startCampus != endCampus
}
