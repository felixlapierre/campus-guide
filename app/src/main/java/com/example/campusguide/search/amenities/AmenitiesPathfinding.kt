package com.example.campusguide.search.amenities

import com.example.campusguide.directions.indoor.Graph
import com.example.campusguide.directions.indoor.IndoorPathfinding
import com.example.campusguide.search.indoor.Node

class AmenitiesPathfinding(graph: Graph, private val filter: String) : IndoorPathfinding(graph) {
    private var listOfBathrooms = mutableListOf<String>()

    override fun isComplete(): Boolean {
        return false
    }

    override fun canVisit(node: Node): Boolean {
        return true
    }

    override fun visit(node: Node) {
        if (node.type == "bathroom") {
            listOfBathrooms.add(node.code)
        }
    }

    /**
     * use the filter so users can search for mens, womens, and gender neutral bathrooms
     */
    override fun getResults(): List<String> {
        val filteredList = mutableListOf<String>()
        listOfBathrooms.map {
            if (it.contains(filter))
                filteredList.add(it)
        }
        return filteredList
    }
}
