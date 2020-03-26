package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Node
import com.google.maps.model.LatLng
import kotlin.math.sqrt

class Pathfinding constructor(graph: Graph) {


    fun findRoom(start: String, target: String) {

    }

    fun approximateDistance(node1: Node, node2: Node): Double {
        val deltaX = node2.x - node1.x
        val deltaY = node2.y - node1.y
        val deltaZ = node2.floor - node1.floor

        return sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
    }
}