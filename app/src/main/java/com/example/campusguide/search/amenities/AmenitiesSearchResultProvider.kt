package com.example.campusguide.search.amenities

import com.example.campusguide.Constants
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

class AmenitiesSearchResultProvider : SearchResultProvider {

    override suspend fun search(query: String): List<SearchResult> {
        if (query.isEmpty()) {
            return emptyList()
        }
        var results: MutableList<SearchResult> = mutableListOf()
        if (Constants.AMENITIES_BATHROOM.startsWith(query.toLowerCase())) {
            results = addWomensBathroom(results)
            results = addMensBathroom(results)
            results = addGenderNeutralBathroom(results)
        } else if (Constants.AMENITIES_WOMENS_BATHROOM.startsWith(query.toLowerCase())) {
            results = addWomensBathroom(results)
        } else if (Constants.AMENITIES_MENS_BATHROOM.startsWith(query.toLowerCase())) {
            results = addMensBathroom(results)
        } else if (Constants.AMENITIES_GENDER_NEUTRAL_BATHROOM.startsWith(query.toLowerCase())) {
            results = addGenderNeutralBathroom(results)
        }
        return results
    }

    fun addMensBathroom(results: MutableList<SearchResult>) : MutableList<SearchResult> {
        results.add(
            SearchResult(
                "Men's Bathroom", "Find a bathroom near me", "amenities_bath_wom"
            )
        )
        return results
    }

    fun addWomensBathroom(results: MutableList<SearchResult>) : MutableList<SearchResult> {
        results.add(
            SearchResult(
                "Women's Bathroom", "Find a bathroom near me", "amenities_bath_mens"
            )
        )
        return results
    }

    fun addGenderNeutralBathroom(results: MutableList<SearchResult>) : MutableList<SearchResult> {
        results.add(
            SearchResult(
                "Gender Neutral Bathroom", "Find a bathroom near me", "amenities_bath_all"
            )
        )
        return results
    }
}
