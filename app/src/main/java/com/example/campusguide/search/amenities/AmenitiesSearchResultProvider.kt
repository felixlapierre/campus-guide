package com.example.campusguide.search.amenities

import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

class AmenitiesSearchResultProvider : SearchResultProvider {

    final val bathroom = "bathroom"

    override suspend fun search(query: String): List<SearchResult> {
        println("--------------------------query: " + query)
        if (query.isEmpty()) {
            println("--------------------------empty")
            return emptyList()
        }
        val results: MutableList<SearchResult> = mutableListOf()
        if (bathroom.startsWith(query.toLowerCase())) {
            println("--------------------------contains")
            results.add(
                SearchResult(
                    "Bathroom", "Find a bathroom near me", "ID"
                )
            )
        }
        println("--------------------------results: "+ results)
        return results
    }
}