package com.example.campusguide.search

import com.example.campusguide.search.indoor.BuildingIndex
import com.example.campusguide.search.indoor.IndoorLocationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking

@RunWith(JUnit4::class)
class IndoorLocationProviderTest {

    @Test
    fun testNotIndoorLocation() = runBlocking {
        val id = "some_not_indoor_id"
        val expected = SearchLocation("someName", "someId", 1.0, 2.0)

        val buildingIndex: BuildingIndex = mock()
        val nextInChain: SearchLocationProvider = mock()
        whenever(nextInChain.getLocation(id)).thenReturn(expected)
        val provider = IndoorLocationProvider(buildingIndex, nextInChain)

        val result = provider.getLocation(id)

        assert(result == expected)
    }
}