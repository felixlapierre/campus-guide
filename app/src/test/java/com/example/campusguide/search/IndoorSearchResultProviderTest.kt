package com.example.campusguide.search

import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.search.indoor.IndoorSearchResultProvider
import com.example.campusguide.search.indoor.Room
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IndoorSearchResultProviderTest {
    private val testIndex: BuildingIndex

    init {
        val fakeRoomOne = Room("fakeRoomOne", "100.00", "1.0", "2.0")
        val fakeRoomTwo = Room("fakeRoomTwo", "200.00", "3.0", "4.0")
        val fakeBuilding = Building(
            "fakeBuilding",
            "BD",
            "fakeAddress",
            "fakeServices",
            "1.0",
            "1.0",
            listOf(fakeRoomOne, fakeRoomTwo),
            listOf()
        )
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

    @Test
    fun searchByRoomName() = runBlocking {
        val provider = IndoorSearchResultProvider(testIndex, 3)
        val results = provider.search("fake")
        assert(results.size == 2)
    }

    @Test
    fun searchByRoomCode() = runBlocking {
        val provider = IndoorSearchResultProvider(testIndex, 3)
        val results = provider.search("100.00")
        assert(results.size == 1)
    }

    @Test
    fun searchYieldsNoResults() = runBlocking {
        val provider = IndoorSearchResultProvider(testIndex, 3)
        val results = provider.search("queryNotMatchingAnything")
        assert(results.isEmpty())
    }

    @Test
    fun searchExceedsCount() = runBlocking {
        val provider = IndoorSearchResultProvider(testIndex, 1)
        val results = provider.search("fake")
        assert(results.size == 1)
    }

    @Test
    fun searchBuildingAndRoomCodesCombined() = runBlocking {
        val provider = IndoorSearchResultProvider(testIndex, 3)
        val results = provider.search("BD-100")
        assert(results.size == 1)
    }
}
