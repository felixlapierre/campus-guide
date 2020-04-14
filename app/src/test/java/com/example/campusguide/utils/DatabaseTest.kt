package com.example.campusguide.utils

import database.entity.Building
import database.entity.Building_
import database.entity.Highlight
import database.entity.Hole
import database.entity.MyObjectBox
import database.entity.Outline
import database.entity.Point
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import io.objectbox.kotlin.boxFor
import java.io.File
import org.junit.After
import org.junit.Assert.assertEquals as assertEquals
import org.junit.Before
import org.junit.Test

class DatabaseTest {
    private val TEST_DIRECTORY: File = File("test-db/objectbox/")
    private var store: BoxStore? = null

    @Before
    fun setUp() { // delete database files before each test to start with a clean database
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
        store =
            MyObjectBox.builder() // add directory flag to change where ObjectBox puts its database files
                .directory(TEST_DIRECTORY) // optional: add debug flags for more detailed ObjectBox log output
                .debugFlags(DebugFlags.LOG_QUERIES or DebugFlags.LOG_QUERY_PARAMETERS)
                .build()
    }

    @After
    fun tearDown() {
        store!!.close()
        store!!.deleteAllFiles()
    }

    @Test
    fun whenAddABuilding_ThenShouldBeInDatabse() {
        // given
        val store = store!!.boxFor<Building>()
        val outlinePoint = Point(1, 10.0, 20.0)
        val holePoint = Point(2, 30.0, 40.0)
        val building = Building(0, "Name", "abbreviation")
        building.highlight.target = Highlight(0)
        val outline = Outline(0)
        outline.points.add(outlinePoint)
        val hole = Hole(0)
        hole.points.add(holePoint)
        outline.holes.add(hole)
        building.highlight.target.outlines.add(outline)

        // when
        store.put(building)

        // then
        val buildings = store.query().equal(Building_.fullName, "Name").build().find()
        assertEquals(1, buildings.size)
        val dbBuilding = buildings[0]
        assertEquals(dbBuilding.fullName, "Name")
        assertEquals(dbBuilding.abbreviationName, "abbreviation")

        assertEquals(dbBuilding.highlight.target.outlines.size, 1)
        val dbOutline = dbBuilding.highlight.target.outlines[0]
        assertEquals(dbOutline.points.size, 1)
        assertEquals(dbOutline.points[0].order, 1)
        assertEquals(dbOutline.points[0].latitude, 10.0, 0.00001)
        assertEquals(dbOutline.points[0].longitude, 20.0, 0.00001)

        assertEquals(dbBuilding.highlight.target.outlines[0].holes.size, 1)
        val dbHole = dbBuilding.highlight.target.outlines[0].holes[0]
        assertEquals(dbHole.points.size, 1)
        assertEquals(dbHole.points[0].order, 2)
        assertEquals(dbHole.points[0].latitude, 30.0, 0.00001)
        assertEquals(dbHole.points[0].longitude, 40.0, 0.00001)
    }
}
