package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.Node

class Graph constructor(building: Building) {
    private val map: MutableMap<String, Node> = mutableMapOf()

    init {
        // Add all rooms to the map
        building.rooms.forEach {room ->
            val floor = room.code[0].toInt()
            map[room.code] = Node(room.code, "Room", room.lat.toDouble(), room.lon.toDouble(), mutableListOf(), floor)
        }

        // Add all nodes to the map
        building.nodes.forEach {node ->
            val floor = node.code[0].toInt()
            node.floor = floor
            map[node.code] = node
        }

        // Ensure symmetry of all edges
        building.nodes.forEach {node ->
            node.edges.forEach { edge ->
                val target = map[edge]
                if(target != null && !target.edges.contains(node.code)) {
                    target.edges.add(node.code)
                }
            }
        }
    }

    fun get(code: String): Node? {
        return map[code]
    }
}