package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.BuildingClickListener
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BuildingClickListenerTest {

    @Test
    fun testBuildingNotFound() {
        val index: BuildingIndex = mock()

        val coords = LatLng(1.0, 1.0)

        whenever(index.getBuildingAtCoordinates(coords)).thenReturn(null)

        val listener = BuildingClickListener(mock(), mock(), index, mock())

        val info = listener.determineBuilding(coords)

        assert(info.symbol == "B")
    }

    @Test
    fun testBuildingFound() {
        val index: BuildingIndex = mock()

        val coords = LatLng(1.0, 1.0)
        val building = Building(
            "someName",
            "someCode",
            "someAddress",
            "someServices",
            "1.0",
            "1.0",
            emptyList()
        )

        whenever(index.getBuildingAtCoordinates(coords)).thenReturn(building)

        val listener = BuildingClickListener(mock(), mock(), index, mock())

        val info = listener.determineBuilding(coords)

        assert(info.symbol == building.code)
    }
}