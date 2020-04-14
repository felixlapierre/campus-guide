package com.example.campusguide.directions

import com.example.campusguide.directions.indoor.Graph
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.Node
import com.example.campusguide.search.indoor.Room
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GraphTest {
    @Test
    fun testEdgesAreSymmetric() {
        val rooms: List<Room> = listOf(Room("room1", "100.00", "1.0", "1.0"))
        val nodes: List<Node> = listOf(Node("basic", "1_node", 0.5, 0.5, mutableListOf("100.00")))
        val building = Building(
            "someBuilding",
            "BD",
            "someAddress",
            "someServices",
            "1.0",
            "2.0",
            rooms,
            nodes
        )

        val graph = Graph(building)

        assertTrue(graph.get("100.00")?.edges?.contains("1_node")!!)
    }
}
