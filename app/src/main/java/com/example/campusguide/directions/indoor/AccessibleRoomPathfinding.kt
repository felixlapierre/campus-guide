package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Node

class AccessibleRoomPathfinding(
    graph: Graph,
    private val forbiddenNodeTypes: List<String>
) : FindRoomPathfinding(graph) {

    override fun canVisit(node: Node): Boolean {
        return forbiddenNodeTypes.all { forbiddenType ->
            forbiddenType != node.type
        }
    }
}
