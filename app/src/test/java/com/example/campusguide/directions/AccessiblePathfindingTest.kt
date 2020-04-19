package com.example.campusguide.directions

import com.example.campusguide.Accessibility
import com.example.campusguide.directions.indoor.AccessibleRoomPathfinding
import com.example.campusguide.directions.indoor.Graph
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.Node
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.nhaarman.mockitokotlin2.mock

@RunWith(JUnit4::class)
class AccessiblePathfindingTest {

    private lateinit var escalatorNode: Node
    private lateinit var elevatorNode: Node
    private lateinit var stairNode: Node
    private lateinit var graph: Graph
    private lateinit var pathfinding: AccessibleRoomPathfinding

    @Before
    fun setupIndoorPath() {
        // initiate nodes of different types that can be forbidden or not
        escalatorNode = Node(
            "escalators", "1_nodeEsc", -73.2, 43.2,
            mutableListOf("1_nodeEle", "102.00")
        )
        elevatorNode = Node(
            "elevators", "1_nodeEle", -73.3, 43.3,
            mutableListOf("1_nodeStr", "103.00")
        )
        stairNode = Node(
            "stairs", "1_nodeStr", -73.4, 43.4,
            mutableListOf("104.00")
        )

        // make a graph to use for AccessibleRoomPathfinding
        val fakeBuilding = Building(
            "someName",
            "someCode",
            "someAddress",
            "someDept",
            "someServices",
            "1.0",
            "2.0",
            listOf(),
            listOf()
        )
        graph = Graph(fakeBuilding)

        // initiate pathfinding to simplify future calls
        pathfinding = AccessibleRoomPathfinding(graph, Accessibility.forbiddenRooms)
    }

    @Test
    fun noNodesForbidden() {
        assert(Accessibility.forbiddenRooms.size == 0)
        assert(pathfinding.canVisit(escalatorNode))
        assert(pathfinding.canVisit(elevatorNode))
        assert(pathfinding.canVisit(stairNode))
    }

    @Test
    fun escalatorsForbidden() {
        Accessibility.setEscalators(false)
        assert(Accessibility.forbiddenRooms.contains("escalators"))
        assert(!this.pathfinding.canVisit(escalatorNode))
    }

    @Test
    fun elevatorsForbidden() {
        Accessibility.setElevators(false)
        assert(Accessibility.forbiddenRooms.contains("elevators"))
        assert(!this.pathfinding.canVisit(elevatorNode))
    }

    @Test
    fun stairsForbidden() {
        Accessibility.setStairs(false)
        Assert.assertTrue(Accessibility.forbiddenRooms.contains("stairs"))
        assert(!this.pathfinding.canVisit(stairNode))
    }

    @Test
    fun allowEscalators() {
        Accessibility.setEscalators(true)
        assert(!Accessibility.forbiddenRooms.contains("escalators"))
        assert(this.pathfinding.canVisit(escalatorNode))
    }

    @Test
    fun allowElevators() {
        Accessibility.setElevators(true)
        assert(!Accessibility.forbiddenRooms.contains("elevators"))
        assert(this.pathfinding.canVisit(elevatorNode))
    }

    @Test
    fun allowStairs() {
        Accessibility.setStairs(true)
        assert(!Accessibility.forbiddenRooms.contains("stairs"))
        assert(this.pathfinding.canVisit(stairNode))
    }
}