package com.example.campusguide.directions.indoor

import com.example.campusguide.map.Map
import com.example.campusguide.search.indoor.BuildingIndex
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline

class IndoorRoute constructor(private val index: BuildingIndex) {
    private var polyline: Polyline? = null
    private var polylineOptions: PolylineOptions? = null

    fun set(buildingCode: String, startRoomCode: String, endRoomCode: String) {
        val path = getPath(buildingCode, startRoomCode, endRoomCode)
        polylineOptions = PolylineOptions()
            .addAll(path)
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
        val pathfinder = Pathfinding(graph)

        return pathfinder.findRoom(startRoomCode, endRoomCode).map { room ->
            val node = graph.get(room)
            LatLng(node!!.x, node!!.y)
        }
    }

    fun display(map: Map) {
        polyline?.remove()
        polyline = map.addPolyline(polylineOptions)
    }

    fun clear() {
        polyline?.remove()
    }
}