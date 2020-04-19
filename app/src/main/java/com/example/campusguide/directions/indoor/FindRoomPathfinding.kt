package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Node
import com.google.android.gms.maps.model.LatLng

open class FindRoomPathfinding(graph: Graph) : IndoorPathfinding(graph) {
    lateinit var target: String
    private var complete = false

    fun findRoom(start: String, target: String): List<List<LatLng>> {
        this.target = target
        return super.findRoom(start).map {
            it.map { node ->
                LatLng(node.y, node.x)
            }
        }
    }

    override fun isComplete(): Boolean {
        return complete
    }

    override fun canVisit(node: Node): Boolean {
        return true
    }

    override fun visit(node: Node) {
        if (node.code == target)
            complete = true
    }

    override fun getResults(): List<String> {
        if (!complete) {
            throw PathNotFoundException("Could not find a path to room $target")
        }
        return listOf(target)
    }
}
