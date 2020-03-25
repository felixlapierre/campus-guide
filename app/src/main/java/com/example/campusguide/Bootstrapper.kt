package com.example.campusguide

import android.view.View
import com.example.campusguide.directions.ChooseDirectionOptionsDialogFragment
import com.example.campusguide.directions.Route
import com.example.campusguide.location.CenterLocationListener
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.SwitchCampus
import com.example.campusguide.map.ChangeFloorUp
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.CustomSearch
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.example.campusguide.utils.permissions.Permissions
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
        GoogleMapInitializer(activity, map, "maps_activity_map")

        //Permissions
        val permissions = Permissions(activity)

        // Search
        val locationProvider = IndoorLocationProvider(
            BuildingIndexSingleton.getInstance(activity.assets),
            PlacesApiSearchLocationProvider(activity)
        )
        val search = CustomSearch(activity, map, locationProvider)
        activity.setOnSearchClickedListener(search)
        activity.addActivityResultListener(search)

        // Center on Location
        val centerLocation = CenterLocationListener(map,
            permissions,
            FusedLocationProvider(activity)
        )
        activity.setOnCenterLocationListener(centerLocation)

        // Change Floor
        val changeFloor = ChangeFloorUp(map)
        activity.setChangeFloorUpListener(changeFloor)

        // Switch Campus
        val switchCampus = SwitchCampus(
            map,
            activity.getCampusNameTextView()
        )
        activity.setSwitchCampusButtonListener(switchCampus)

        // Navigation
        val route = Route(map, activity)
        activity.setOnNavigateListener(View.OnClickListener{
            val chooseDirectionOptions = ChooseDirectionOptionsDialogFragment()
            chooseDirectionOptions.show(activity.supportFragmentManager, "directionsOptions")
        })
    }
}