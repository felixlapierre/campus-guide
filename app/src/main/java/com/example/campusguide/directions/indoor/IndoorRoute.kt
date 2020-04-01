package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.BuildingIndex
import com.google.android.gms.maps.model.LatLng

class IndoorRoute constructor(private val index: BuildingIndex) {
    private var line: List<LatLng> = emptyList()

    fun set(buildingCode: String, startRoomCode: String, endRoomCode: String) {
        line = getPath(buildingCode, startRoomCode, endRoomCode)
    }

    private fun getPath(
        buildingCode: String,
        startRoomCode: String,
        endRoomCode: String
    ): List<LatLng> {
        val building =
            index.getBuildings()?.find { building ->
                building.code == buildingCode
            }
                ?: throw RuntimeException("Building $buildingCode was not found in the index")

        val graph = Graph(building)
        val pathfinder = FindRoomPathfinding(graph)

        return pathfinder.findRoom(startRoomCode, endRoomCode)[0].map { room ->
            val node = graph.get(room)
            LatLng(node!!.y, node!!.x)
        }
    }

    fun getLine(): List<LatLng> {
        return line
    }
}