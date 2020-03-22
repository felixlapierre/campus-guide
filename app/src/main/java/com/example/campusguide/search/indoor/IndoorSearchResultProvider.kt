package com.example.campusguide.search.indoor

import android.app.Activity
import com.beust.klaxon.Klaxon
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

class IndoorSearchResultProvider constructor(private val activity: Activity, private val count: Int):
    SearchResultProvider {
    private val buildings: MutableList<BuildingIndex> = mutableListOf()

    init {
        val path = "index"
        activity.assets.list(path)?.forEach {it ->
            val contents = activity.assets.open("$path/$it").bufferedReader().use {it.readText()}
            val building = Klaxon().parse<BuildingIndex>(contents)
            if(building != null)
                buildings.add(building)
        }
    }

    override suspend fun search(query: String): List<SearchResult> {
        if(query.isEmpty())
            return emptyList()

        val results: MutableList<SearchResult> = mutableListOf()
        val adjustedQuery = query.toLowerCase()
        buildings.forEach { building ->
            building.rooms.forEach {room ->
                if(room.name.toLowerCase().startsWith(adjustedQuery)
                    || room.code.startsWith(adjustedQuery)) {
                    val secondaryText = "${building.name} ${building.code}${room.code}"
                    results.add(SearchResult(
                        room.name, secondaryText, room.code
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