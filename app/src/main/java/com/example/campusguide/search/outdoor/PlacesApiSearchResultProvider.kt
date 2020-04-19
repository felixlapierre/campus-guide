package com.example.campusguide.search.outdoor

import android.app.Activity
import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.location.Location
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Gets search results using Google's Places API.
 */
class PlacesApiSearchResultProvider constructor(activity: Activity, private val count: Int = Int.MAX_VALUE) :
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
        }.addOnFailureListener { exception ->
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

            if (results.count() >= count) {
                return results
            }
        }

        return results
    }

    suspend fun searchNearbyPlaces(
        pointOfInterest: String,
        currentLocation: Location
    ) = suspendCoroutine<FindAutocompletePredictionsResponse> { cont ->

        val searchBounds = RectangularBounds.newInstance(
            LatLng(currentLocation.lat - 0.01, currentLocation.lon - 0.01),
            LatLng(currentLocation.lat + 0.01, currentLocation.lon + 0.01)
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(pointOfInterest)
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setLocationRestriction(searchBounds)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->

            cont.resume(getPlaces(response))
        }
    }

    private fun getPlaces(response: FindAutocompletePredictionsResponse): FindAutocompletePredictionsResponse {
            return(response)
    }
}
