package com.example.campusguide.directions

import android.Manifest
import android.location.Location
import android.provider.Settings.Global.putInt
import android.widget.Button
import com.example.campusguide.R
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.utils.permissions.PermissionsSubject
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.fragment.app.testing.launchFragment as launchFragment

@RunWith(JUnit4::class)
class ChooseOriginOptionsTest {

    @Test
    fun whenClickOnCurrentLocationWithPermission_ThenGetUsersLocation(){
        val scenario = launchFragment<ChooseOriginOptions>()
        //given
        val permissionsSubject: PermissionsSubject = mock()
        val fusedLocationProvider: FusedLocationProvider = mock()

        var locationExpected = Location("Montreal")
        locationExpected.latitude = 45.4972159
        locationExpected.longitude = -73.6103642
        var locationActual = Location("center")
        locationActual.latitude = 0.00
        locationActual.longitude = 0.00
        whenever(fusedLocationProvider.getLocation(any())).then{invocation ->
            val callback  = invocation.arguments[0] as ((Location) -> Unit)
            callback(locationExpected)
        }
        whenever(permissionsSubject.havePermission(Manifest.permission.ACCESS_FINE_LOCATION)).thenReturn(true)

        val dialog = ChooseOriginOptions(permissionsSubject, fusedLocationProvider) { location ->
            locationActual = location
        }

        //when
        //assertNotNull(dialog.activity)
        dialog.requireActivity()
        dialog.activity!!.findViewById<Button>(R.id.currentLocation).performClick()

        //then
        assertEquals(locationExpected, locationActual)
    }
}