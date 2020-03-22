package com.example.campusguide.search.indoor

import android.app.Activity
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

class IndoorSearchResultProvider constructor(private val activity: Activity, private val count: Int):
    SearchResultProvider {

    override suspend fun search(query: String): List<SearchResult> {
        val buildings = BuildingIndexSingleton.getInstance(activity).getBuildings()
        if(query.isEmpty() || buildings == null)
            return emptyList()

        val results: MutableList<SearchResult> = mutableListOf()
        val adjustedQuery = query.toLowerCase()
        buildings.forEach { building ->
            building.rooms.forEach {room ->
                if(room.name.toLowerCase().startsWith(adjustedQuery)
                    || room.code.startsWith(adjustedQuery)) {
                    val secondaryText = "${building.name} ${building.code}${room.code}"
                    val placeId = "indoor_${building.code}_${room.code}"
                    results.add(SearchResult(
                        room.name, secondaryText, placeId
                    ))
                    if(results.count() >= count) {
                        return results
                    }
                }
            }
        }

        return results;
    }
}