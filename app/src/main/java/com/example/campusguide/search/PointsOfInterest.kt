package com.example.campusguide.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.campusguide.MapsActivity
import com.example.campusguide.R
import com.example.campusguide.directions.DirectionsFlow
import com.example.campusguide.location.Location
import com.example.campusguide.location.LocationProvider
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.outdoor.PlacesApiSearchResultProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PointsOfInterest(
    private val activity: MapsActivity,
    private val locationProvider: LocationProvider,
    private val map: GoogleMapAdapter,
    private val directionsFlow: DirectionsFlow
) : DialogFragment() {

    private val placesClient: PlacesClient
    var locationListener: SearchLocationListener? = null

    private val nearbyLocations = PlacesApiSearchResultProvider(activity)

    init {
        if (!Places.isInitialized())
            Places.initialize(
                activity.applicationContext,
                activity.getString(R.string.google_maps_key)
            )

        placesClient = Places.createClient(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.points_of_interest_options, container, false)

        view.findViewById<Button>(R.id.cafe)?.setOnClickListener {
            dismiss();
            searchPOINearCurrentLocation("Cafe");
        }
        view.findViewById<Button>(R.id.restaurant)?.setOnClickListener {
            dismiss();
            searchPOINearCurrentLocation("Restaurant");
        }
        view.findViewById<Button>(R.id.shopping)?.setOnClickListener {
            dismiss();
            searchPOINearCurrentLocation("Shopping mall");
        }
        view.findViewById<Button>(R.id.pharmacy)?.setOnClickListener {
            dismiss();
            searchPOINearCurrentLocation("Pharmacy");
        }
        view.findViewById<Button>(R.id.grocerystore)?.setOnClickListener {
            dismiss();
            searchPOINearCurrentLocation("Grocery store");
        }
        return view
    }

    /**
     * Gets the user's last known most recent location
     */

    private fun searchPOINearCurrentLocation(poi: String) {
        return locationProvider.getLocation {
            GlobalScope.launch {
                getNearbyPlaces(poi, it)
            }
        }
    }

    private suspend fun getNearbyPlaces(poi: String, location: Location) {
        activity.runOnUiThread {
            PopupSearchLocationListener.clearAllMarkers()
        }

        val places = nearbyLocations.searchNearbyPlaces(poi, location)
        displayNearbyLocations(places)
    }

    private fun displayNearbyLocations(places : FindAutocompletePredictionsResponse) {
        for (place in places.autocompletePredictions) {

            val placeID = place.placeId
            val placeFields: List<Place.Field> =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            val request = FetchPlaceRequest.newInstance(placeID, placeFields)

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val placeResponse = response.place

                    val searchLocation = SearchLocation(
                        placeResponse.name!!,
                        placeResponse.latLng!!.latitude,
                        placeResponse.latLng!!.longitude,
                        placeResponse.id!!,
                        placeResponse.address?:""
                    )

                    var popupSearchLocationListener = PopupSearchLocationListener(
                        activity as AppCompatActivity,
                        directionsFlow,
                        map
                    )

                    popupSearchLocationListener.onLocation(searchLocation)

                    placeResponse.latLng?.let { map.animateCamera(it, 14.0f) }
                }
        }
    }
}