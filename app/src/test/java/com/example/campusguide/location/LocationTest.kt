package com.example.campusguide.location

import com.example.campusguide.search.indoor.IndoorLocation
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocationTest {
    @Test
    fun testEncodingLocation() {
        val lat = 45.2354264
        val lon = -73.235246246
        val location = Location("someName", lat, lon)
        Assert.assertEquals("$lat, $lon", location.encodeForDirections())
    }

    @Test
    fun testEncodingIndoorLocation() {
        val lat = 43.11353151
        val lon = -75.121435153
        val id = "indoor_LB_100.00"
        val location: Location = IndoorLocation("someName", lat, lon, id, "someSecondaryText")
        Assert.assertEquals(id, location.encodeForDirections())
    }
}