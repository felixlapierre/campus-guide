package com.example.campusguide.search.amenities

import com.example.campusguide.directions.indoor.Graph
import com.example.campusguide.directions.indoor.IndoorPathfinding
import com.example.campusguide.search.indoor.Node

class AmenitiesPathfinding(graph: Graph) : IndoorPathfinding(graph) {
    private var listOfBathrooms = mutableListOf<String>()

    override fun isComplete(): Boolean {
        return false
    }

    override fun canVisit(node: Node): Boolean {
        return true
    }

    override fun visit(node: Node) {
        if(node.type == "bathroom") {
            listOfBathrooms.add(node.code)
        }
    }

    override fun getResults(filter: String): List<String> {
        val filteredList = mutableListOf<String>()
        listOfBathrooms.map {
            if(it.contains(filter))
                filteredList.add(it)
        }
        return filteredList
    }
}