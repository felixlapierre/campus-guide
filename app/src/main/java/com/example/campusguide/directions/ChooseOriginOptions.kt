package com.example.campusguide.directions

import android.Manifest
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.calendar.Events
import com.example.campusguide.calendar.FindEventLocation
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.search.CustomSearch
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationListener
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.indoor.IndoorSearchResultProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.example.campusguide.utils.permissions.PermissionsSubject
import database.ObjectBox
import database.entity.Calendar
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class ChooseOriginOptions(
    private val permission: PermissionsSubject,
    private val locationProvider: FusedLocationProvider,
    private val locationSelectedListener: (location: Location) -> Unit
) : DialogFragment() {

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.choose_origin_options, container, false)

        view.findViewById<Button>(R.id.currentLocation)?.setOnClickListener {
            useCurrentLocation()
        }

        view.findViewById<Button>(R.id.search)?.setOnClickListener {
            searchForLocation()
        }

        view.findViewById<Button>(R.id.calendar)?.setOnClickListener {
            useLastEventLocation()
        }

        return view
    }

    /**
     * Gets the user's last known most recent location
     */
    private fun useCurrentLocation() {
        if (permission.havePermission(locationPermission)) {
            dismiss()
            locationProvider.getLocation { location ->
                locationSelectedListener(location)
            }
        } else {
            permission.requestPermission(locationPermission)
        }
    }

    /**
     * Allows users to manually enter start and end point
     */
    private fun searchForLocation() {
        dismiss()
        // Build the CustomSearch
        val act = activity as MapsActivity
        val provider = IndoorLocationProvider(
            BuildingIndexSingleton.getInstance(act.assets),
            PlacesApiSearchLocationProvider(activity!!)
        )
        val search = CustomSearch(act, provider, Constants.ORIGIN_SEARCH_REQUEST_CODE)

        search.setLocationListener {searchLocation ->
            if(searchLocation != null) {
                val location = Location(searchLocation.name)
                location.latitude = searchLocation.lat
                location.longitude = searchLocation.lon
                locationSelectedListener(location)
            }
            act.removeActivityResultListener(search)
        }
        act.addActivityResultListener(search)
        search.openCustomSearchActivity()
    }

    private fun useLastEventLocation(){
        val calendarBox: Box<Calendar> = ObjectBox.boxStore.boxFor()
        val mycal = Pair(calendarBox.all[0].id, calendarBox.all[0].name)

        var lastLocation = Events(activity as MapsActivity, mycal).getLastEventLocation()

        GlobalScope.launch {
            FindEventLocation(activity as FragmentActivity, locationSelectedListener).getLocationOfEvent(lastLocation)
        }
    }
}