package com.example.campusguide.search.indoor

import android.app.Activity
import com.beust.klaxon.Klaxon
import com.example.campusguide.search.SearchResult
import com.example.campusguide.search.SearchResultProvider

class IndoorSearchResultProvider constructor(private val activity: Activity):
    SearchResultProvider {
    private val buildings: MutableList<BuildingIndex> = mutableListOf()

    init {
        activity.assets.list("index")?.forEach {it ->
            val contents = activity.assets.open(it).bufferedReader().use {it.readText()}
            val building = Klaxon().parse<BuildingIndex>(contents)
            if(building != null)
                buildings.add(building)
        }
    }

    override suspend fun search(query: String): List<SearchResult> {
        val results: MutableList<SearchResult> = mutableListOf()

        buildings.forEach { building ->
            building.rooms.forEach {room ->
                if(room.name.startsWith(query)
                    || room.code.startsWith(query)) {
                    results.add(SearchResult(
                        room.name, room.code, room.code
                    ))
                }
            }
        }

        return results;
    }
}