package com.example.campusguide.calendar

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.Location
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.amenities.AmenitiesLocationProvider
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.indoor.IndoorSearchResultProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchResultProvider
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.permissions.Permissions

class FindEventLocation constructor(
    private val activity: FragmentActivity,
    private val locationListener: (location: Location) -> Unit
) {
    private val permissions = Permissions(activity)
    private val fusedLocationProvider = FusedLocationProvider(activity)
    private val buildingIndex: BuildingIndex = BuildingIndexSingleton.getInstance(activity.assets)
    private val indoorSearch = IndoorSearchResultProvider(buildingIndex)
    private val outdoorSearch = PlacesApiSearchResultProvider(activity)
    //chain of responsibility
    private val locationProvider = IndoorLocationProvider(
        buildingIndex,
        AmenitiesLocationProvider(
            PlacesApiSearchLocationProvider(activity),
            permissions,
            activity as AppCompatActivity,
            fusedLocationProvider
        )
    )

    suspend fun getLocationOfEvent(eventLocation: String?) {
        if (eventLocation == null) {
            DisplayMessageErrorListener(activity).onError("Could not find any events today")
        } else {
            val location = find(eventLocation)
            if (location != null)
                locationListener(location)
            else {
                DisplayMessageErrorListener(activity).onError("Could not find the event location")
            }
        }
    }

    private suspend fun find(eventLocation: String): Location? {
        if (eventLocation.isEmpty()) return null

        var searchResult: SearchResult? = null
        val indoorResults = indoorSearch.search(eventLocation)
        if (indoorResults.isNotEmpty()) {
            searchResult = indoorResults[0]
        } else {
            val outdoorResults = outdoorSearch.search(eventLocation)
            if (outdoorResults.isNotEmpty())
                searchResult = outdoorResults[0]
        }
        if (searchResult == null) return null
        return locationProvider.getLocation(searchResult.id) ?: return null
    }
}
