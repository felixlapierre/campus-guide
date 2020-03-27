package com.example.campusguide.directions.indoor

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusguide.search.indoor.Node
import java.lang.RuntimeException
import java.util.*
import kotlin.math.sqrt

class Pathfinding constructor(private val graph: Graph) {
    lateinit var openSet: Queue<String>
    val nodeData: MutableMap<String, NodeData> = mutableMapOf()

    // Priority queue requires api version 24 for some reason
    @RequiresApi(Build.VERSION_CODES.N)
    fun findRoom(start: String, target: String): List<String> {
        if(graph.get(start) == null) {
            throw NonexistentLocationException("Location $start was not found in the graph")
        }

        val open = PriorityQueue<String>() { s1, s2 ->
            return@PriorityQueue calculatePriority(s1, s2, target)
        }

        openSet = open
        open.add(start)
        nodeData.clear()
        graph.forEach {code ->
            nodeData[code] = NodeData()
        }
        nodeData[start]!!.cheapest = 0.0

        while(open.isNotEmpty()) {
            if(open.first() == target) {
                return reconstructPath(start, target)
            }
            iterate(target)
        }

        throw PathNotFoundException("Could not find a path from $start to $target")
    }

    fun calculatePriority(s1: String, s2: String, target: String): Int {
        val node1 = graph.get(s1)
        val node2 = graph.get(s2)
        val targetNode = graph.get(target)

        val comparison = approximateDistance(node1, targetNode) - approximateDistance(node2, targetNode)
        if(comparison < 0) return -1
        else if(comparison > 0) return 1
        else return 0
    }

    fun approximateDistance(node1: Node?, node2: Node?): Double {
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
            val neighbor = graph.get(it)!!
            val neighborData = nodeData[it]!!

            val length = currData.cheapest + approximateDistance(currNode, neighbor)
            if(length < neighborData.cheapest) {
                neighborData.cameFrom = curr
                neighborData.cheapest = length
                neighborData.estimated = neighborData.cheapest + approximateDistance(neighbor, graph.get(target))
                if(!openSet.contains(it)) {
                    openSet.add(it)
                }
            }
        }

    }

    private fun reconstructPath(start: String, end: String): List<String> {
        var current = end;
        val totalPath: MutableList<String> = mutableListOf(end)
        while(nodeData[current]!!.cameFrom != null) {
            current = nodeData[current]!!.cameFrom!!
            totalPath.add(0, current)
        }
        return totalPath
    }

    data class NodeData(
        var cameFrom: String? = null,
        var cheapest: Double = Double.MAX_VALUE,
        var estimated: Double = Double.MAX_VALUE
    )
}

class PathNotFoundException(message: String) : RuntimeException(message)

class NonexistentLocationException(message: String) : RuntimeException(message)