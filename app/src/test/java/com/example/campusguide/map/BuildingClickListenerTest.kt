package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.BuildingClickListener
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
@RunWith(JUnit4::class)
class BuildingClickListenerTest {
    lateinit var index: BuildingIndex
    lateinit var map: Map
    lateinit var points: ArrayList<LatLng>

    @Before
    fun setUp() {
        index = mock()
        map = mock()
        points = arrayListOf(LatLng(1.0, 1.0), LatLng(1.0, 2.0), LatLng(2.0, 1.0), LatLng(2.0, 2.0))
    }

    @Test
    fun testBuildingFound() {
        val listener = BuildingClickListener(map, index, mock())

        val marker: IMarker? = mock()
        whenever(map.addMarker(any())).thenReturn(marker)
        whenever(index.getBuildingAtCoordinates(LatLng(1.5, 1.5))).thenReturn(
            Building(
                "Hall",
                "address",
                "Code",
                "departments",
                "services",
                "",
                "",
                listOf(),
                listOf()
            )
        )

        listener.polygonClick(points)

        verify(marker)?.setTag(any())
        verify(marker)?.showInfoWindow()
    }

    @Test
    fun testBuildingNotFound() {
        val listener = BuildingClickListener(map, index, mock())

        val marker: IMarker? = mock()
        whenever(map.addMarker(any())).thenReturn(marker)
        whenever(index.getBuildingAtCoordinates(LatLng(1.5, 1.5))).thenReturn(null)

        listener.polygonClick(points)

        verify(marker)?.setTag(any())
        verify(marker)?.showInfoWindow()
    }
}
