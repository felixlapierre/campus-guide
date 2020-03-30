package com.example.campusguide.location

import android.widget.TextView
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SwitchCampusTest {

    private lateinit var mockMap : Map
    private lateinit var mockCampusName : TextView

    @Before
    fun createMocks() {
        mockMap = mock()
        mockCampusName = mock()
    }


    @Test
    fun testSwitchCampusLoyola() {

        val listener = SwitchCampus(mockMap, mockCampusName)
        val loyolaLat = 45.458153
        val loyolaLong = -73.640490
        val mockIsChecked = true

        listener.onCheckedChanged(mock(), mockIsChecked)

        val expectedLatLng = LatLng(loyolaLat, loyolaLong)

        verify(mockMap).animateCamera(expectedLatLng, Constants.ZOOM_STREET_LVL)
        verify(mockCampusName).text = Constants.LOYOLA_CAMPUS

    }

    @Test
    fun testSwitchCampusSGW() {
        val listener = SwitchCampus(mockMap, mockCampusName)
        val SGWLat = 45.495792
        val SGWLong = -73.578096
        val mockIsChecked = false

        listener.onCheckedChanged(mock(), isChecked = mockIsChecked)

        val expectedLatLng = LatLng(SGWLat, SGWLong)

        verify(mockMap).animateCamera(expectedLatLng, Constants.ZOOM_STREET_LVL)
        verify(mockCampusName).text = Constants.SGW_CAMPUS
    }

}