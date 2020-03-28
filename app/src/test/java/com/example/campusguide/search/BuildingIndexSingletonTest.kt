package com.example.campusguide.search

import com.example.campusguide.search.indoor.*
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BuildingIndexSingletonTest {
    @Test
    fun testGettingInstanceTwiceReturnsSameInstance() {
        val instance1 = BuildingIndexSingleton.getInstance(mock())
        val instance2 = BuildingIndexSingleton.getInstance(mock())

        assert(instance1 == instance2)
    }

    @Test
    fun testGettingBuildingsReturnsNullWhenUnloaded() {
        val instance = BuildingIndexSingleton.getInstance(mock())
        val buildings = instance.getBuildings()
        assert(buildings == null)
    }
}