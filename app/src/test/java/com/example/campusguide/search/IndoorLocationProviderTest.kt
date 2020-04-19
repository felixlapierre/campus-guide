package com.example.campusguide.search

import com.example.campusguide.Constants
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.search.indoor.BuildingNotFoundException
import com.example.campusguide.search.indoor.IdFormatException
import com.example.campusguide.search.indoor.IndexNotLoadedException
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.indoor.Room
import com.example.campusguide.search.indoor.RoomNotFoundException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IndoorLocationProviderTest {
    private val indoorPrefix = Constants.INDOOR_LOCATION_IDENTIFIER

    private val fakeIndex: BuildingIndex
    private val fakeBuilding: Building
    private val fakeRoom: Room

    init {
        // Create fake test building data
        val fakeBuildingCode = "BD"
        fakeRoom = Room("someRoomName", "420.69", "3.0", "2.0")
        fakeBuilding =
            Building(
                "someBuildingName",
                fakeBuildingCode,
                "someAddress",
                "departments",
                "someServices",
                "1.0",
                "1.0",
                listOf(fakeRoom),
                listOf()
            )

        fakeIndex = mock()
        whenever(fakeIndex.getBuildings()).thenReturn(listOf(fakeBuilding))
    }

    @Test
    fun testNotIndoorLocation() = runBlocking {
        val id = "some_not_indoor_id"
        val expected = SearchLocation("someName", 1.0, 2.0, "someId", "secondaryTest")

        val buildingIndex: BuildingIndex = mock()
        val nextInChain: SearchLocationProvider = mock()
        whenever(nextInChain.getLocation(id)).thenReturn(expected)
        val provider = IndoorLocationProvider(buildingIndex, nextInChain)

        val result = provider.getLocation(id)

        assert(result == expected)
    }

    @Test
    fun testIndoorLocationFound() = runBlocking {
        val searchId = "${indoorPrefix}_${fakeBuilding.code}_${fakeRoom.code}"

        val searchLocationProvider = IndoorLocationProvider(fakeIndex, mock())

        var searchResult = searchLocationProvider.getLocation(searchId)
        assert(searchResult != null)
        searchResult = searchResult as SearchLocation
        assert(searchResult.id == searchId)
        assert(searchResult.name == fakeRoom.name)
        assert(searchResult.lat == fakeRoom.lat.toDouble())
        assert(searchResult.lon == fakeRoom.lon.toDouble())
    }

    @Test(expected = IndexNotLoadedException::class)
    fun testIndexNotLoaded() = runBlocking<Unit> {
        // Create fake test building data
        val fakeUnloadedIndex: BuildingIndex = mock()
        whenever(fakeUnloadedIndex.getBuildings()).thenReturn(null)

        val searchId = "${indoorPrefix}_someBuildingCode_someRoomCode"

        val searchLocationProvider = IndoorLocationProvider(fakeUnloadedIndex, mock())

        searchLocationProvider.getLocation(searchId)
    }

    @Test(expected = BuildingNotFoundException::class)
    fun testBuildingNotFound() = runBlocking<Unit> {
        // Create fake test building data
        val nonexistentBuildingCode = "ab"
        val searchId = "${indoorPrefix}_${nonexistentBuildingCode}_${fakeRoom.code}"

        val searchLocationProvider = IndoorLocationProvider(fakeIndex, mock())

        searchLocationProvider.getLocation(searchId)
    }

    @Test(expected = RoomNotFoundException::class)
    fun testRoomNotFound() = runBlocking<Unit> {
        // Create fake test building data
        val nonexistentRoomCode = "489.00"
        val searchId = "${indoorPrefix}_${fakeBuilding.code}_$nonexistentRoomCode"

        val searchLocationProvider = IndoorLocationProvider(fakeIndex, mock())

        searchLocationProvider.getLocation(searchId)
    }

    @Test(expected = IdFormatException::class)
    fun testIdBadFormat() = runBlocking<Unit> {
        val badId = "${indoorPrefix}_${fakeBuilding.code}"

        val searchLocationProvider = IndoorLocationProvider(fakeIndex, mock())

        searchLocationProvider.getLocation(badId)
    }
}
