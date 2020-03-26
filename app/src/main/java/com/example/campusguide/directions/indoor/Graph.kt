package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.Node
import com.google.maps.model.LatLng

class Graph constructor(building: Building) {
    private val map: MutableMap<String, Node> = mutableMapOf()

    init {
        // Add all rooms to the map
        building.rooms.forEach {room ->
            val floor = room.code[0].toInt()
            map[room.code] = Node(room.code, "Room", room.lon.toDouble(), room.lat.toDouble(), mutableListOf(), floor)
        }

        // Add all nodes to the map
        building.nodes.forEach {node ->
            val floor = node.code[0].toInt()
            node.floor = floor
            val latlng = toLatLng(node.x, node.y)
            node.x = latlng.lng
            node.y = latlng.lat
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

    private val lbtopleft = LatLng(45.496682, -73.578693)
    private val lbtopright = LatLng(45.497308, -73.578065)
    private val lbbotleft = LatLng(45.496270, -73.577757)

    fun toLatLng(x: Double, y: Double): LatLng {
        val intermediate = add(lbtopleft, lbtopright, x)
        return add(intermediate, lbbotleft, y)
    }

    fun add(coords1: LatLng, coords2: LatLng, quotient: Double): LatLng {
        val latdiff = coords2.lat - coords1.lat
        val latitude = coords1.lat + quotient * latdiff

        val londiff = coords2.lng - coords1.lng
        val longitude = coords1.lng + quotient * londiff

        return LatLng(latitude, longitude)
    }
}