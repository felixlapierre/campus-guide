package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Node
import com.google.android.gms.maps.model.LatLng

class FindRoomPathfinding(graph: Graph): IndoorPathfinding(graph) {
    lateinit var target: String
    private var complete = false

    override fun findRoom(start: String, target: String): List<List<LatLng>> {
        this.target = target
        return super.findRoom(start, "")
    }

    override fun isComplete(): Boolean {
        return complete
    }

    override fun canVisit(node: Node): Boolean {
        return true
    }

    override fun visit(node: Node) {
        if(node.code == target)
            complete = true
    }

    override fun getResults(filter: String): List<String> {
        if(!complete) {
            throw PathNotFoundException("Could not find a path to room $target")
        }
        return listOf(target)
    }
}