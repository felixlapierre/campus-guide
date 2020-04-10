package com.example.campusguide.search.amenities

import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

class AmenitiesSearchResultProvider : SearchResultProvider {

    final val bathroom = "bathroom"
    final val women = "women's bathroom"
    final val men = "men's bathroom"
    final val unisex = "unisex bathroom"

    override suspend fun search(query: String): List<SearchResult> {
        if (query.isEmpty()) {
            return emptyList()
        }
        val results: MutableList<SearchResult> = mutableListOf()
        if (bathroom.startsWith(query.toLowerCase())) {
            results.add(
                SearchResult(
                    "Women's Bathroom", "Find a bathroom near me", "amenities_bath_wom"
                )
            )
            results.add(
                SearchResult(
                    "Men's Bathroom", "Find a bathroom near me", "amenities_bath_men"
                )
            )
            results.add(
                SearchResult(
                    "Unisex Bathroom", "Find a bathroom near me", "amenities_bath_uni"
                )
            )
        } else if (women.startsWith(query.toLowerCase())) {
            results.add(
                SearchResult(
                    "Women's Bathroom", "Find a bathroom near me", "amenities_bath_wom"
                )
            )
        } else if (men.startsWith(query.toLowerCase())) {
            results.add(
                SearchResult(
                    "Men's Bathroom", "Find a bathroom near me", "amenities_bath_men"
                )
            )
        } else if (unisex.startsWith(query.toLowerCase())) {
            results.add(
                SearchResult(
                    "Unisex Bathroom", "Find a bathroom near me", "amenities_bath_uni"
                )
            )
        }
        return results
    }
}