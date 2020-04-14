package com.example.campusguide.utils

import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PolygonUtilsTest {
    @Test
    fun testPolygonCenter() {
        val points: ArrayList<LatLng> = arrayListOf()

        points.add(LatLng(1.0, 1.0))
        points.add(LatLng(2.0, 1.0))
        points.add(LatLng(1.0, 2.0))
        points.add(LatLng(2.0, 2.0))

        val center = PolygonUtils().getPolygonCenterPoint(points)

        assert(center == LatLng(1.5, 1.5))
    }
}
