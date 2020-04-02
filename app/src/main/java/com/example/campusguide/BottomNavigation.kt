package com.example.campusguide

import android.content.Intent
import com.example.campusguide.directions.ChooseDestinationOptions
import com.example.campusguide.directions.ChooseOriginOptions
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.utils.permissions.Permissions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.maps.model.LatLng

class BottomNavigation constructor (
    private val activity: MapsActivity,
    private val map: GoogleMapAdapter,
    private val permissions: Permissions,
    private val locationProvider: FusedLocationProvider) {

    private lateinit var bottomNavigationView : BottomNavigationView

    fun setupBottomNavigation() {
        bottomNavigationView = activity.findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.campus_map
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.directions -> {
                    val chooseDestinationOptions = ChooseDestinationOptions { destination ->
                        val chooseOriginOptions =
                            ChooseOriginOptions(permissions, locationProvider) { origin ->
                                val originLatLng = LatLng(origin.latitude, origin.longitude)
                                val destinationLatLng =
                                    LatLng(destination.latitude, destination.longitude)
                                val intent =
                                    Intent(activity, DirectionsActivity::class.java).apply {
                                        putExtra("Origin", originLatLng.toString())
                                        putExtra("Destination", destinationLatLng.toString())
                                    }
                                activity.startActivity(intent)
                            }
                        chooseOriginOptions.show(
                            activity.supportFragmentManager,
                            "chooseOriginOptions"
                        )
                    }
                    chooseDestinationOptions.show(
                        activity.supportFragmentManager,
                        "chooseDestinationOptions"
                    )
                }
                R.id.campus_map -> {
                    val hall = com.google.android.gms.maps.model.LatLng(45.495792, -73.578096)
                    map.animateCamera(hall, Constants.ZOOM_STREET_LVL)
                }
                R.id.poi -> {
                    // points of interest view
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}