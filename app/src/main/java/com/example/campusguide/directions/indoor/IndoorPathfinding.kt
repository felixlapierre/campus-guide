package com.example.campusguide.directions.indoor

import com.example.campusguide.Constants
import com.example.campusguide.search.indoor.Node
import java.lang.RuntimeException
import java.util.ArrayDeque
import java.util.Queue
import kotlin.math.sqrt

abstract class IndoorPathfinding constructor(private val graph: Graph) {
    private lateinit var openSet: Queue<String>
    private val nodeData: MutableMap<String, NodeData> = mutableMapOf()

    // Priority queue requires api version 24 for some reason
    open fun findRoom(start: String): List<List<Node>> {
        if (graph.get(start) == null) {
            throw NonexistentLocationException("$start " + Constants.LOCATION_NOT_FOUND_GRAPH)
        }

        // TODO: Determine a way to use a priority queue here instead
        val open = ArrayDeque<String>()

        openSet = open
        open.add(start)
        nodeData.clear()
        graph.forEach { code ->
            nodeData[code] = NodeData()
        }
        nodeData[start]!!.cheapest = 0.0

        while (!isComplete() && open.isNotEmpty()) {
            iterate()
        }

        val results = getResults().map {
            reconstructPath(it)
        }

        return results.sortedWith(Comparator { l1, l2 ->
            l1.size - l2.size
        })
    }

    abstract fun isComplete(): Boolean
    abstract fun canVisit(node: Node): Boolean
    abstract fun visit(node: Node)
    abstract fun getResults(): List<String>

    // TODO: Reinstate priority queue mechanism

    private fun approximateDistance(node1: Node?, node2: Node?): Double {
        if (node1 == null || node2 == null)
            return Double.MAX_VALUE
        val deltaX = node2.x - node1.x
        val deltaY = node2.y - node1.y
        val deltaZ = node2.floor - node1.floor

        return sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
    }

    private fun iterate() {
        val curr = openSet.remove()!!
        val currNode = graph.get(curr)!!
        val currData = nodeData[curr]!!
        currNode.edges.forEach {
            val neighbor = graph.get(it)
                ?: throw NonexistentLocationException(Constants.LOCATION_NOT_FOUND_ROOM + "$it")
            val neighborData = nodeData[it]!!

            val length = currData.cheapest + approximateDistance(currNode, neighbor)
            if (canVisit(neighbor) && length < neighborData.cheapest) {
                visit(neighbor)
                neighborData.cameFrom = curr
                neighborData.cheapest = length
                if (!openSet.contains(it)) {
                    openSet.add(it)
                }
            }
        }
    }

    private fun reconstructPath(end: String): List<Node> {
        var current = end
        val totalPath: MutableList<Node> = mutableListOf(graph.get(end)!!)
        while (nodeData[current]!!.cameFrom != null) {
            current = nodeData[current]!!.cameFrom!!
            totalPath.add(0, graph.get(current)!!)
        }
        return totalPath
    }

    data class NodeData(
        var cameFrom: String? = null,
        var cheapest: Double = Double.MAX_VALUE
    )
}

class NonexistentLocationException(message: String) : RuntimeException(message)

class PathNotFoundException(message: String) : RuntimeException(message)
