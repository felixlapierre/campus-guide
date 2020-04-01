package com.example.campusguide.directions.indoor

import com.example.campusguide.search.indoor.Node
import com.google.android.gms.maps.model.LatLng
import java.lang.RuntimeException
import java.util.*
import kotlin.math.sqrt

abstract class IndoorPathfinding constructor(private val graph: Graph) {
    lateinit var openSet: Queue<String>
    val nodeData: MutableMap<String, NodeData> = mutableMapOf()

    // Priority queue requires api version 24 for some reason
    open fun findRoom(start: String, target: String): List<List<LatLng>> {
        if(graph.get(start) == null) {
            throw NonexistentLocationException("Location $start was not found in the graph")
        }

        // TODO: Determine a way to use a priority queue here instead
        val open = ArrayDeque<String>()

        openSet = open
        open.add(start)
        nodeData.clear()
        graph.forEach {code ->
            nodeData[code] = NodeData()
        }
        nodeData[start]!!.cheapest = 0.0

        while(!isComplete() && open.isNotEmpty()) {
            iterate(target)
        }

        return getResults().map {
            reconstructPath(it)
        }
    }

    abstract fun isComplete(): Boolean
    abstract fun canVisit(node: Node): Boolean
    abstract fun visit(node: Node)
    abstract fun getResults(): List<String>

    fun calculatePriority(s1: String, s2: String, target: String): Int {
        val node1 = graph.get(s1)
        val node2 = graph.get(s2)
        val targetNode = graph.get(target)

        val comparison = approximateDistance(node1, targetNode) - approximateDistance(node2, targetNode)
        if(comparison < 0) return -1
        else if(comparison > 0) return 1
        else return 0
    }

    private fun approximateDistance(node1: Node?, node2: Node?): Double {
        if(node1 == null || node2 == null)
            return Double.MAX_VALUE
        val deltaX = node2.x - node1.x
        val deltaY = node2.y - node1.y
        val deltaZ = node2.floor - node1.floor

        return sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
    }

    private fun iterate(target: String) {
        val curr = openSet.remove()!!
        val currNode = graph.get(curr)!!
        val currData = nodeData[curr]!!
        currNode.edges.forEach {
            val neighbor = graph.get(it)
                ?: throw NonexistentLocationException("Could not find room: $it")
            val neighborData = nodeData[it]!!

            val length = currData.cheapest + approximateDistance(currNode, neighbor)
            if(canVisit(neighbor) && length < neighborData.cheapest) {
                visit(neighbor)
                neighborData.cameFrom = curr
                neighborData.cheapest = length
                neighborData.estimated = neighborData.cheapest + approximateDistance(neighbor, graph.get(target))
                if(!openSet.contains(it)) {
                    openSet.add(it)
                }
            }
        }

    }

    private fun reconstructPath(end: String): List<LatLng> {
        var current = end;
        val totalPath: MutableList<LatLng> = mutableListOf(getCoordinatesOfNode(end))
        while(nodeData[current]!!.cameFrom != null) {
            current = nodeData[current]!!.cameFrom!!
            totalPath.add(0, getCoordinatesOfNode(current))
        }
        return totalPath
    }

    private fun getCoordinatesOfNode(code: String): LatLng {
        val node = graph.get(code) ?: throw NonexistentLocationException("Could not find room: $code")
        return LatLng(node!!.y, node!!.x)
    }

    data class NodeData(
        var cameFrom: String? = null,
        var cheapest: Double = Double.MAX_VALUE,
        var estimated: Double = Double.MAX_VALUE
    )
}

class NonexistentLocationException(message: String) : RuntimeException(message)