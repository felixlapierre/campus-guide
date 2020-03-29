package com.example.campusguide

import CustomInfoWindow
import android.content.Intent
import android.view.View
import com.example.campusguide.calendar.Login
import com.example.campusguide.directions.ChooseDestinationOptions
import com.example.campusguide.directions.ChooseOriginOptions
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.location.CenterLocationListener
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.SwitchCampus
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.map.SearchLocationMarker
import com.example.campusguide.map.infoWindow.BuildingClickListener
import com.example.campusguide.search.CustomSearch
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.example.campusguide.utils.permissions.Permissions
import com.google.maps.model.LatLng
import database.ObjectBox

/**
 * Bootstrapper sets up the application by adding event listeners to the
 * activity and injecting dependencies.
 */
class Bootstrapper constructor(activity: MapsActivity) {
    init {
        // Local Database
        ObjectBox.init(activity.applicationContext)

        //Permissions
        val permissions = Permissions(activity)
        activity.permissions = permissions

        val locationProvider = FusedLocationProvider(activity)

        // Directions
        val directions = DirectionsFlow(activity, permissions, locationProvider)

        // Map
        val map = GoogleMapAdapter()
        val buildingClickListener = BuildingClickListener(
            map,
            BuildingIndexSingleton.getInstance(activity.assets),
            CustomInfoWindow(activity),
            directions
        )
        GoogleMapInitializer(activity, map, "maps_activity_map", buildingClickListener)

        // Search
        val searchLocationProvider = IndoorLocationProvider(
            BuildingIndexSingleton.getInstance(activity.assets),
            PlacesApiSearchLocationProvider(activity)
        )
        val search =
            CustomSearch(
                activity,
                searchLocationProvider,
                Constants.REGULAR_SEARCH_REQUEST_CODE
            )
        search.locationListener = SearchLocationMarker(activity, map)
        activity.setOnSearchClickedListener(search)
        activity.addActivityResultListener(search)

        // Center on Location
        val centerLocation = CenterLocationListener(
            map,
            permissions,
            locationProvider
        )
        activity.setOnCenterLocationListener(centerLocation)

        // Switch Campus
        val switchCampus = SwitchCampus(
            map,
            activity.getCampusNameTextView()
        )
        activity.setSwitchCampusButtonListener(switchCampus)

        // Navigation
        activity.setOnNavigateListener(View.OnClickListener {
            val chooseDestinationOptions = ChooseDestinationOptions { destination ->
                val chooseOriginOptions =
                    ChooseOriginOptions(permissions, locationProvider) { origin ->
                        val originLatLng = LatLng(origin.latitude, origin.longitude)
                        val destinationLatLng = LatLng(destination.latitude, destination.longitude)
                        val intent = Intent(activity, DirectionsActivity::class.java).apply {
                            putExtra("Origin", originLatLng.toString())
                            putExtra("Destination", destinationLatLng.toString())
                        }
                        activity.startActivity(intent)
                    }
                chooseOriginOptions.show(activity.supportFragmentManager, "chooseOriginOptions")
            }
            chooseDestinationOptions.show(
                activity.supportFragmentManager,
                "chooseDestinationOptions"
            )
        })

        // Login
        val login = Login(activity, permissions)
        login.onCreate()
        login.onStart()
        activity.addActivityResultListener(login)

        // Drawer
        val drawer = Drawer(activity, login)
        drawer.setupDrawer()
    }
}