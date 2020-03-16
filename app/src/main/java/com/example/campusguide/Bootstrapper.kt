package com.example.campusguide

import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.campusguide.directions.*
import com.example.campusguide.location.CenterLocationListener
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.SwitchCampus
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.map.GoogleMapInitializer
import com.example.campusguide.search.Search
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.permissions.Permissions
import com.google.android.gms.maps.SupportMapFragment
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
        val centerLocation = CenterLocationListener(map,
            permissions,
            FusedLocationProvider(activity)
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
                val chooseOriginOptions = ChooseOriginOptions(route) { origin ->
                    val getDirectionsDialogFragment =
                        GetDirectionsDialogFragment(
                            GetDirectionsDialogFragment.DirectionsDialogOptions(
                                "Choose",
                                origin.toString(),
                                destination.toString(),
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