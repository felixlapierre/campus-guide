package com.example.campusguide.search.outdoor

import android.app.Activity
import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.location.Location
import com.example.campusguide.map.GoogleMapAdapter
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Gets search results using Google's Places API.
 */
class PlacesApiSearchResultProvider constructor(activity: Activity, private val count: Int = Int.MAX_VALUE):
    SearchResultProvider {
    private val placesClient: PlacesClient

    init {
        if (!Places.isInitialized())
            Places.initialize(activity.applicationContext, activity.getString(R.string.google_maps_key))

        placesClient = Places.createClient(activity)
    }

    override suspend fun search(query: String) = suspendCoroutine<List<SearchResult>> { cont ->
        val token = AutocompleteSessionToken.newInstance()

        val searchBounds = RectangularBounds.newInstance(
            LatLng(Constants.SEARCH_BOTTOM_BOUND, Constants.SEARCH_LEFT_BOUND),
            LatLng(Constants.SEARCH_TOP_BOUND, Constants.SEARCH_RIGHT_BOUND)
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setLocationRestriction(searchBounds)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            cont.resume(toSearchResults(response))
        }.addOnFailureListener{ exception ->
            cont.resumeWithException(exception)
        }
    }

    private fun toSearchResults(response: FindAutocompletePredictionsResponse): List<SearchResult> {
        val results: MutableList<SearchResult> = mutableListOf()

        response.autocompletePredictions.forEach { it ->
            val primaryText = it.getPrimaryText(null).toString()
            val secondaryText = it.getSecondaryText(null).toString()
            val id = it.placeId
            val searchResult = SearchResult(
                primaryText,
                secondaryText,
                id
            )

            results.add(searchResult)

            if(results.count() >= count) {
                return results
            }
        }

        return results
    }

    // distance between latitudes and longitudes
    private fun customeHaversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double, dist : Double): Double {

        var dist = dist
        var lat1 = lat1
        var lat2 = lat2
        var lon1 = lon1
        var lon2 = lon2
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)
        lon2 = Math.toRadians(lon2)

        //----------


        val rad = 6371.0


        dist /= (2 * rad)


        // convert to radians
        // apply formulae
        val a = sin((lat2 - lat1) / 2).pow(2.0) + sin((lon2 - lon1) / 2).pow(2.0) * cos(lat1) * cos(lat2)
        val c = 2 * asin(sqrt(a))

        return rad * c
    }

    fun searchNearbyPlaces(
        currentLocation: Location,
        map: GoogleMapAdapter
    ) {
        val searchBounds = RectangularBounds.newInstance(
            LatLng(currentLocation.lat - 0.05, currentLocation.lon - 0.05),
            LatLng(currentLocation.lat + 0.05, currentLocation.lon + 0.05)
        )


        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery("coffee, cafe")
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setLocationRestriction(searchBounds)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (place in response.autocompletePredictions) {
                val placeID = place.placeId
                val placeFields: List<Place.Field> =
                    listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                val request = FetchPlaceRequest.newInstance(placeID, placeFields)

                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response: FetchPlaceResponse ->
                        val placeResponse = response.place
                        placeResponse.latLng?.let {
                            placeResponse.name?.let { it1 ->
                                map.addMarker(
                                    it,
                                    it1
                                )
                            }
                        }
                        placeResponse!!.latLng?.let { map.animateCamera(it, 10.0f) }
                    }
            }
        }
    }
}