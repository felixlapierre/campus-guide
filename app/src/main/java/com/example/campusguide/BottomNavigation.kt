package com.example.campusguide

import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.PointsOfInterest
import com.example.campusguide.utils.permissions.Permissions
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigation constructor (
    private val activity: MapsActivity,
    private val map: GoogleMapAdapter,
    private val directions: DirectionsFlow
) {

    private var bottomNavigationView : BottomNavigationView =
        activity.findViewById(R.id.bottom_navigation)

    init {
        bottomNavigationView.selectedItemId = R.id.campus_map
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.directions -> {
                    // TODO: Use DirectionsFlow class instead
                    startNavigation()
                }
                R.id.campus_map -> {
                    centerCameraSGW()
                }
                R.id.poi -> {
                    // TODO: Switch to POI list view as part of UC-47
                    choosePOI();
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun centerCameraSGW(){
        map.animateCamera(Constants.SGW_COORDINATES, Constants.ZOOM_STREET_LVL)
    }

    private fun startNavigation() {
        directions.startFlow()
    }

    private fun choosePOI() {
        val permissions = Permissions(activity)
        activity.permissions = permissions

        val locationProvider = FusedLocationProvider(activity)

        val pointsOfInterest = PointsOfInterest(activity, locationProvider, map)
        pointsOfInterest.show(activity.supportFragmentManager, "chooseOriginOptions")
    }
}
