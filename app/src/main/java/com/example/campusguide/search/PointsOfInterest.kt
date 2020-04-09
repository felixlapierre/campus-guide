package com.example.campusguide.search

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.Location
import com.example.campusguide.location.LocationProvider
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.outdoor.PlacesApiSearchResultProvider
import com.example.campusguide.utils.permissions.PermissionsSubject
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.android.synthetic.main.choose_origin_options.*

class PointsOfInterest constructor(
    activity : MapsActivity,
    private val permission: PermissionsSubject,
    private val locationProvider: LocationProvider
    ) : DialogFragment() {

    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    //private val act = activity as MapsActivity
    private val nearbyLocations = PlacesApiSearchResultProvider(activity)
    val cafe = Place.Type.CAFE


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.points_of_interest_options, container, false)

        view.findViewById<Button>(R.id.GetNearbyPlaces)?.setOnClickListener {
            useCurrentLocation();
        }

        return view
    }

    /**
     * Gets the user's last known most recent location
     */

    private fun useCurrentLocation() {
        return locationProvider.getLocation {
            getNearbyPlaces(it)
        }
    }

    private fun getNearbyPlaces(location : Location) {
        nearbyLocations.searchNearbyPlaces(location)

        //
        // val searchBounds = RectangularBounds.newInstance(
        //     LatLng(Constants.SEARCH_BOTTOM_BOUND, Constants.SEARCH_LEFT_BOUND),
        //     LatLng(Constants.SEARCH_TOP_BOUND, Constants.SEARCH_RIGHT_BOUND)
        // )
        //
        // val request = FindAutocompletePredictionsRequest.builder()
        //     .setTypeFilter(TypeFilter.REGIONS.apply { cafe })
        //     .setLocationRestriction(searchBounds)
        //     .build()
        //
        //
        //
        // val placesClient = context?.let { Places.createClient(it) };
        //
        // val placeFields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME)
        //
        // placesClient
        //
        // placesClient?.fetchPlace(FetchPlaceRequest.newInstance(locationPermission, MutableList()))



    }

}