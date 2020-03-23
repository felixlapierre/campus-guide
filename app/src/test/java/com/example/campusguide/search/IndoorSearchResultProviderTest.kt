package com.example.campusguide.search

import com.example.campusguide.search.indoor.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking

@RunWith(JUnit4::class)
class IndoorSearchResultProviderTest {
    private val testIndex: BuildingIndex

    init {
        val fakeRoomOne = Room("fakeRoomOne", "100.00", "1.0", "2.0")
        val fakeRoomTwo = Room("fakeRoomTwo", "200.00", "3.0", "4.0")
        val fakeBuilding = Building("fakeBuilding", "bd", "fakeAddress", listOf(fakeRoomOne, fakeRoomTwo))
        testIndex = mock()
        whenever(testIndex.getBuildings()).thenReturn(listOf(fakeBuilding))
    }

    @Test
    fun searchEmptyQuery() = runBlocking {
        val provider = IndoorSearchResultProvider(testIndex, 3)
        val results = provider.search("")
        assert(results.isEmpty())
    }

    @Test
    fun searchBuildingIndexNotLoaded() = runBlocking {
        val unloadedIndex: BuildingIndex = mock()
        whenever(unloadedIndex.getBuildings()).thenReturn(null)
        val provider = IndoorSearchResultProvider(unloadedIndex, 3)
        val results = provider.search("someQuery")
        assert(results.isEmpty())
    }
}