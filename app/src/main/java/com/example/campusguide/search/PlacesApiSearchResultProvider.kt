package com.example.campusguide.search

import android.app.Activity
import com.example.campusguide.R
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PlacesApiSearchResultProvider constructor(activity: Activity) {
    private val placesClient: PlacesClient

    init {
        if (!Places.isInitialized())
            Places.initialize(activity.applicationContext, activity.getString(R.string.google_maps_key))

        placesClient = Places.createClient(activity)
    }
    suspend fun search(query: String) = suspendCoroutine<FindAutocompletePredictionsResponse> { cont ->
        val token = AutocompleteSessionToken.newInstance()

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            cont.resume(response)
        }.addOnFailureListener{ exception ->
            cont.resumeWithException(exception)
        }
    }
}