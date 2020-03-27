package com.example.campusguide.directions

import com.example.campusguide.directions.indoor.Graph
import com.example.campusguide.directions.indoor.NonexistentLocationException
import com.example.campusguide.directions.indoor.PathNotFoundException
import com.example.campusguide.directions.indoor.Pathfinding
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.Node
import com.example.campusguide.search.indoor.Room
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PathfindingTest {
    lateinit var graph: Graph

    @Before
    fun setupGraph() {
        val rooms: List<Room> = listOf(
            Room("room1", "100.00", "1.0", "2.0"),
            Room("room2", "102.00", "3.0", "4.0"),
            Room("room3", "103.00", "5.0", "6.0")
        )

        val nodes: List<Node> = listOf(
            Node("nodeA", "1_nodeA", 0.525, 0.665, mutableListOf("1_nodeB", "100.00")),
            Node("nodeB", "1_nodeB", 0.625, 0.665, mutableListOf("1_nodeC", "102.00")),
            Node("nodeC", "1_nodeC", 0.725, 0.665, mutableListOf("103.00"))
        )
        val building = Building("someName", "someCode", "someAddress", rooms, nodes)
        graph = Graph(building)
    }

    @Test
    fun findStraightLine() {
        val pathfinder = Pathfinding(graph)
        val path = pathfinder.findRoom("1_nodeA", "103.00")
        Assert.assertArrayEquals(arrayOf("1_nodeA", "1_nodeB", "1_nodeC", "103.00"), path.toTypedArray())
    }

    @Test(expected = PathNotFoundException::class)
    fun testPathNotFound() {
        val pathfinder = Pathfinding(graph)
        pathfinder.findRoom("1_nodeC", "nonexistent")
    }

    @Test(expected = NonexistentLocationException::class)
    fun testStartLocationDoesNotExist() {
        val pathfinder = Pathfinding(graph)
        pathfinder.findRoom("nonexistent", "100.00")
    }
}