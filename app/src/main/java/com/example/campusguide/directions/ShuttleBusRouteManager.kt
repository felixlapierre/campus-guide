package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.model.TravelMode

class ShuttleBusRouteManager constructor(
    private val startLocation: LatLng,
    private val endLocation: LatLng
) {
    private enum class Campus {
        NOT_ON_CAMPUS,
        DOWNTOWN,
        LOYOLA
    }

    val DOWNTOWN_SHUTTLE_COORDINATES_STRING = "45.497183,-73.578443"
    val LOYOLA_SHUTTLE_COORDINATES_STRING = "45.458424,-73.638369"
    // TODO: Decide on less arbitrary bounds for the campuses
    val DOWNTOWN_CAMPUS_BOUNDS: LatLngBounds = LatLngBounds(
        LatLng(45.497707, -73.571275), LatLng(45.493656, -73.584395)
    )
    val LOYOLA_CAMPUS_BOUNDS: LatLngBounds = LatLngBounds(
        LatLng(45.454483, -73.634354), LatLng(45.461610, -73.645662)
    )
    private val startCampus: Campus
    private val endCampus: Campus
    private val startLocationString: String
    private val endLocationString: String

    init {
        startCampus = getCampusFromLatLng(startLocation)
        endCampus = getCampusFromLatLng(endLocation)
        startLocationString = "${startLocation.latitude},${startLocation.longitude}"
        endLocationString = "${endLocation.latitude},${endLocation.longitude}"
    }

    public fun setDowntownOf(route: Route, travelMode: TravelMode) {
        if (startCampus == Campus.DOWNTOWN)
            route.set(startLocationString, DOWNTOWN_SHUTTLE_COORDINATES_STRING, travelMode)
        else if (startCampus == Campus.LOYOLA)
            route.set(DOWNTOWN_SHUTTLE_COORDINATES_STRING, endLocationString, travelMode)
        else
            route.set(startLocationString, endLocationString, travelMode)
    }
    public fun setLoyolaOf(route: Route, travelMode: TravelMode) {
        if (startCampus == Campus.LOYOLA && endCampus == Campus.DOWNTOWN)
            route.set(startLocationString, LOYOLA_SHUTTLE_COORDINATES_STRING, travelMode)
        else if (startCampus == Campus.DOWNTOWN && endCampus == Campus.LOYOLA)
            route.set(LOYOLA_SHUTTLE_COORDINATES_STRING, endLocationString, travelMode)
        else
            route.set(startLocationString, endLocationString, travelMode)
    }
    public fun shouldBeShuttleRoute(): Boolean {
        return startCampus != Campus.NOT_ON_CAMPUS &&
                endCampus != Campus.NOT_ON_CAMPUS &&
                startCampus != endCampus
    }

    private fun getCampusFromLatLng (location: LatLng): Campus {
        return when {
            DOWNTOWN_CAMPUS_BOUNDS.contains(location) -> {
                Campus.DOWNTOWN;
            }
            LOYOLA_CAMPUS_BOUNDS.contains(location) -> {
                Campus.LOYOLA;
            }
            else -> {
                Campus.NOT_ON_CAMPUS;
            }
        }
    }


}