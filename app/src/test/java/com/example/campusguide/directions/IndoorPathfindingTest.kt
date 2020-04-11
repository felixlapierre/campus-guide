package com.example.campusguide.directions

import com.example.campusguide.directions.indoor.*
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.Node
import com.example.campusguide.search.indoor.Room
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IndoorPathfindingTest {
    lateinit var graph: Graph
    lateinit var rooms: List<Room>
    lateinit var nodes: List<Node>

    @Before
    fun setupGraph() {
        rooms = listOf(
            Room("room1", "100.00", "1.0", "2.0"),
            Room("room2", "102.00", "3.0", "4.0"),
            Room("room3", "103.00", "5.0", "6.0")
        )

        nodes = listOf(
            Node("nodeA", "1_nodeA", -73.1, 43.1, mutableListOf("1_nodeB", "100.00")),
            Node("nodeB", "1_nodeB", -73.2, 43.2, mutableListOf("1_nodeC", "102.00")),
            Node("nodeC", "1_nodeC", -73.3, 43.3, mutableListOf("103.00"))
        )
        val building = Building("someName", "someCode", "someAddress", "someServices", "1.0", "2.0", rooms, nodes)
        graph = Graph(building)
    }

    @Test
    fun findStraightLine() {
        val pathfinder = FindRoomPathfinding(graph)
        val paths = pathfinder.findRoom("1_nodeA", "103.00")
        Assert.assertEquals(paths.size, 1)
        val path = paths[0]
        Assert.assertEquals(path.size, 4 )

        val expectedPath = arrayOf("1_nodeA", "1_nodeB", "1_nodeC", "103.00").map {
                val node = graph.get(it)!!
                LatLng(node.y, node.x)
            }
        for((index, element) in path.withIndex()) {
            assert(element == expectedPath[index])
        }
    }

    @Test
    fun findAdjacent() {
        val pathfinder = FindRoomPathfinding(graph)
        val paths = pathfinder.findRoom("1_nodeB", "102.00")
        Assert.assertEquals(paths.size, 1)
        val path = paths[0]
        Assert.assertEquals(path.size, 2)

        val expectedPath = arrayOf("1_nodeB", "102.00").map {
            val node = graph.get(it)!!
            LatLng(node.y, node.x)
        }
        for((index, element) in path.withIndex()) {
            assert(element == expectedPath[index])
        }
    }

    @Test(expected = NonexistentLocationException::class)
    fun testTargetNonexistent() {
        val pathfinder = FindRoomPathfinding(graph)
        pathfinder.findRoom("1_nodeC", "nonexistent")
    }

    @Test(expected = NonexistentLocationException::class)
    fun testStartLocationDoesNotExist() {
        val pathfinder = FindRoomPathfinding(graph)
        pathfinder.findRoom("nonexistent", "100.00")
    }
}