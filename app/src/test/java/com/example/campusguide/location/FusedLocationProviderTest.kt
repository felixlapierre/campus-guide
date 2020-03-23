package com.example.campusguide.location

import android.app.Activity
import android.location.Location
import com.example.campusguide.directions.DirectionsDialogConfirmationListener
import com.example.campusguide.directions.EmptyDirectionsGuard
import com.example.campusguide.utils.ErrorListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.callbackFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class FusedLocationProviderTest {



//    @Mock lateinit var mockActivity: Activity
//
//    private lateinit var fusedLocationProvider: FusedLocationProvider
//
//    @Before
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//        fusedLocationProvider = FusedLocationProvider(mockActivity)
//    }

    val mockActivity : Activity = mock()

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(mockActivity)

    @Test
    fun testGetLocationWhenNull() {

        val mockLocation : Location = mock()
        val mockFusedLocationClient : FusedLocationProviderClient = mock()
        val listener = fusedLocationClient

        whenever(listener.lastLocation.addOnSuccessListener { mockLocation }).thenAnswer {
            val callback = it as (Location) -> Unit
            callback(mockLocation)
        }

    }

}
