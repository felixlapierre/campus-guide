package com.example.campusguide.search

/**
 * Provides the latitude / longitude information for a search result,
 * using the ID of the search result.
 */
interface SearchLocationProvider {
    suspend fun getLocation(id: String): SearchLocation
}