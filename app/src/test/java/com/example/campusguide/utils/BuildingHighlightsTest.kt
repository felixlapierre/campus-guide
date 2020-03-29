package com.example.campusguide.utils

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.*
import database.entity.Point
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BuildingHighlightsTest {

    @Test
    fun testGetLatLngList() {
        var points : Collection<Point> = mock()

        val latLngs = mutableListOf<LatLng>()
        assert(latLngs.isEmpty())

        for (point in points.sortedBy { it.order }) {
            var counter : Int = 0
            latLngs.add(LatLng(point.latitude, point.longitude))
            assert(latLngs[counter++] == LatLng(point.latitude, point.longitude))
        }

    }

}
