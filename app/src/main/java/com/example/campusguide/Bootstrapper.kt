package com.example.campusguide

import android.view.View
import com.example.campusguide.directions.*
import com.example.campusguide.location.CenterLocationListener
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.SwitchCampus
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.Search
import com.example.campusguide.utils.DisplayMessageErrorListener
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

        // Map
        val map = GoogleMapAdapter()
        GoogleMapInitializer(activity, map)

        //Permissions
        val permissions = Permissions(activity)

        // Search
        val search = Search(activity, map)
        activity.setOnSearchClickedListener(search)
        activity.addActivityResultListener(search)

        // Center on Location
        val locationProvider = FusedLocationProvider(activity)
        val centerLocation = CenterLocationListener(map,
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
        val route = Route(map, activity)
        activity.setOnNavigateListener(View.OnClickListener{
            val chooseDestinationOptions = ChooseDestinationOptions { destination ->
                val chooseOriginOptions = ChooseOriginOptions(route, permissions, locationProvider) { origin ->
                    val originLatLng = LatLng(origin.latitude, origin.latitude)
                    val destinationLatLng = LatLng(origin.longitude, origin.latitude)
                    val getDirectionsDialogFragment =
                        GetDirectionsDialogFragment(
                            GetDirectionsDialogFragment.DirectionsDialogOptions(
                                "Confirm start and end location",
                                originLatLng.toString(),
                                destinationLatLng.toString(),
                                EmptyDirectionsGuard(
                                    CallbackDirectionsConfirmListener { start, end ->
                                        //Display the directions time
                                        route?.set(start, end)
                                    },
                                    DisplayMessageErrorListener(activity)
                                )
                            )
                        )
                    getDirectionsDialogFragment.show(activity.supportFragmentManager, "directionsDialog")
                }
                chooseOriginOptions.show(activity.supportFragmentManager, "chooseOriginOptions")
            }
            chooseDestinationOptions.show(activity.supportFragmentManager, "chooseDestinationOptions")
        })
    }
}