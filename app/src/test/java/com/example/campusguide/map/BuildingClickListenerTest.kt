package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.BuildingClickListener
import com.example.campusguide.search.indoor.BuildingIndex
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BuildingClickListenerTest {

    @Test
    fun testBuildingNotFound() {
        val points : ArrayList<LatLng> = arrayListOf(LatLng(1.0, 1.0), LatLng(1.0, 2.0), LatLng(2.0, 1.0), LatLng(2.0, 2.0))

        val index: BuildingIndex = mock()
        val map: Map = mock()

        val listener = BuildingClickListener(map, index, mock())
        listener.polygonClick(points)




        //listener.determineBuilding(coords)


//        val info = listener.determineBuilding(coords)
//
//        assert(info.symbol == "B")
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

    @Test
    fun testAddMarker() {
        val points: ArrayList<LatLng> =
            arrayListOf(LatLng(1.0, 1.0), LatLng(1.0, 2.0), LatLng(2.0, 1.0), LatLng(2.0, 2.0))

        val index: BuildingIndex = mock()
        val map: Map = mock()

        val listener = BuildingClickListener(map, index, mock())
        listener.polygonClick(points)

        whenever(map.addMarker(any())).then { it ->
            it.getArgument(0)

        }
    }
}