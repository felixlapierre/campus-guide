package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.LatLng

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
    builder.include(LatLng(45.496789, -73.573031)).include(LatLng(45.459212,-73.641706))
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

public fun shouldBeShuttleRoute(start: LocationMetadata, end: LocationMetadata): Boolean {
    val startCampus = campusFromLatLng(start.getLatLng())
    val endCampus = campusFromLatLng(end.getLatLng())
    return startCampus != Campus.OFF_CAMPUS &&
        endCampus != Campus.OFF_CAMPUS &&
        startCampus != endCampus
}

public fun campusFromLatLng (location: LatLng): Campus {
    return when {
        DOWNTOWN_CAMPUS_BOUNDS().contains(location) -> {
            println("It is in SGW!!")
            Campus.DOWNTOWN
        }
        LOYOLA_CAMPUS_BOUNDS().contains(location) -> {
            println("It is in LOY!!")
            Campus.LOYOLA
        }
        else -> {
            println("SGW bounds: ${DOWNTOWN_CAMPUS_BOUNDS()}")
            println("LOY bounds: ${LOYOLA_CAMPUS_BOUNDS()}")
            println("This location is off campus: ${location.longitude}, ${location.latitude}")
            Campus.OFF_CAMPUS
        }
    }
}