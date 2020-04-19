package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Node
import com.google.android.gms.maps.model.LatLng

open class FindRoomPathfinding(graph: Graph) : IndoorPathfinding(graph) {
    lateinit var target: String
    private var complete = false

    fun findRoom(start: String, target: String): List<List<IndoorPath>> {
        this.target = target
        val list = super.findRoom(start).map {
            val list = mutableListOf<IndoorPath>()
            it.forEach { node ->
                val point = LatLng(node.y, node.x)
                if (list.size == 0 || list[list.size - 1].floor != node.floor) {
                    list.add(IndoorPath(mutableListOf(point), node.floor))
                } else {
                    list[list.size - 1].points.add(point)
                }
                Unit
            }
            list
        }
        return list
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
