package com.example.campusguide.search.indoor

import android.app.Activity
import com.example.campusguide.Constants
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

/**
 * Gets search results based on the contents of local JSON files
 * containing information about indoor rooms in buildings.
 */
class IndoorSearchResultProvider constructor(
    private val index: BuildingIndex,
    private val count: Int = Int.MAX_VALUE
) :
    SearchResultProvider {

    override suspend fun search(query: String): List<SearchResult> {
        val buildings = index.getBuildings()
        if (query.isEmpty() || buildings == null)
            return emptyList()

        val results: MutableList<SearchResult> = mutableListOf()
        buildings.forEach { building ->
            building.rooms.forEach { room ->
                if (matchesQuery(building, room, query)) {
                    val secondaryText = "${building.name} ${building.code}-${room.code}"
                    val prefix = Constants.INDOOR_LOCATION_IDENTIFIER
                    val placeId = "${prefix}_${building.code}_${room.code}"
                    results.add(
                        SearchResult(
                            room.name, secondaryText, placeId
                        )
                    )
                    if (results.count() >= count) {
                        return results
                    }
                }
            }
        }

        return results;
    }

    private fun matchesQuery(building: Building, room: Room, query: String): Boolean {
        val adjustedQuery = query.toLowerCase().replace("-", "")

        return room.name.toLowerCase().startsWith(adjustedQuery)
                || room.code.startsWith(adjustedQuery)
                || "${building.code.toLowerCase()}${room.code}".startsWith(adjustedQuery)
    }
}