package com.example.campusguide.directions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.search.CustomSearch
import com.example.campusguide.search.SearchLocation
import com.example.campusguide.search.SearchLocationListener
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class ChooseDestinationOptions(private val locationSelectedListener: (location: Location) -> Unit): DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.choose_destination_options, container, false)

        view.findViewById<Button>(R.id.calendar).setOnClickListener {
            useNextEvent()
        }
        view.findViewById<Button>(R.id.fromMap).setOnClickListener {
            chooseFromMap()
        }
        view.findViewById<Button>(R.id.search).setOnClickListener {
            searchForLocation()
        }

        return view
    }

    private fun useNextEvent(){
        //TODO fill this method once we have the logic to use a calendar Event
        dismiss()
        val location = Location("Montreal")
        location.latitude = 45.5017
        location.longitude = -73.5673
        locationSelectedListener(location)
    }

    private fun chooseFromMap(){
        //TODO fill this method once we have the logic to select from the map
        dismiss()
        val location = Location("Montreal")
        location.latitude = 45.5017
        location.longitude = -73.5673
        locationSelectedListener(location)
    }

    private fun searchForLocation(){
        dismiss()
        // Build the CustomSearch
        val act = activity as MapsActivity
        val provider = IndoorLocationProvider(
            BuildingIndexSingleton.getInstance(act.assets),
            PlacesApiSearchLocationProvider(activity!!)
        )
        val search = CustomSearch(act, provider, Constants.DESTINATION_SEARCH_REQUEST_CODE)

        search.setLocationListener {searchLocation ->
            val location = Location(searchLocation.name)
            location.latitude = searchLocation.lat
            location.longitude = searchLocation.lon
            locationSelectedListener(location)
            act.removeActivityResultListener(search)
        }

        act.addActivityResultListener(search)
        search.openCustomSearchActivity()
    }
}