package com.example.campusguide.location

import android.Manifest
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.example.campusguide.utils.permissions.PermissionsSubject
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class CenterLocationListenerTest {
    val map: Map = mock()
    val permissions: PermissionsSubject = mock()
    val locationProvider: LocationProvider = mock()
    val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    @Test
    fun testPermissionDenied() {
        val listener = CenterLocationListener(map, permissions, locationProvider)

        whenever(permissions.havePermission(locationPermission)).thenReturn(false)

        listener.onClick(mock())

        verify(permissions).requestPermission(locationPermission)
        verify(map, never()).addMarker(any(), anyString())
    }

    @Test
    fun testAddMarkersAndCenterMap() {
        val listener = CenterLocationListener(map, permissions, locationProvider)

        val mockLocation: Location = Location("someLocation", 10.0, 12.0)

        whenever(permissions.havePermission(locationPermission)).thenReturn(true)
        whenever(locationProvider.getLocation(any())).thenAnswer {
            val callback = it.arguments[0] as (Location) -> Unit
            callback(mockLocation)
        }

        listener.onClick(mock())

        val expectedLatLng = LatLng(mockLocation.lat, mockLocation.lon)

        verify(map).addMarker(expectedLatLng, Constants.LOCATION_MARKER_TITLE)
        verify(map).animateCamera(expectedLatLng, Constants.ZOOM_STREET_LVL)
    }
}
