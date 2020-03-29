package com.example.campusguide.calendar

import android.location.Location
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.indoor.IndoorSearchResultProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchResultProvider
import com.example.campusguide.utils.DisplayMessageErrorListener

class FindEventLocation constructor(
    private val activity: FragmentActivity,
    private val locationListener: (location: Location) -> Unit
) {
    private val buildingIndex: BuildingIndex = BuildingIndexSingleton.getInstance(activity.assets)
    private val indoorSearch = IndoorSearchResultProvider(buildingIndex)
    private val outdoorSearch = PlacesApiSearchResultProvider(activity)
    private val locationProvider = IndoorLocationProvider(
        buildingIndex,
        PlacesApiSearchLocationProvider(activity)
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
        val searchLocation = locationProvider.getLocation(searchResult.id) ?: return null
        val result = Location("event")
        result.latitude = searchLocation.lat
        result.longitude = searchLocation.lon
        return result
    }
}