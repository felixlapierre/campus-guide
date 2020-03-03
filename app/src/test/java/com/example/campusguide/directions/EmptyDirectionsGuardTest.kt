package com.example.campusguide.directions

import androidx.appcompat.app.AppCompatActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class EmptyDirectionsGuardTest() {

    @Mock lateinit var activity: AppCompatActivity
    @Mock lateinit var wrapped: GetDirectionsDialogFragment.DirectionsDialogConfirmationListener

    lateinit var emptyDirectionsGuard: EmptyDirectionsGuard

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        emptyDirectionsGuard = EmptyDirectionsGuard(activity, wrapped)
    }

    @Test
    fun onConfirmCalled_WhenStringsEmpty() {
        
        val start = "not empty"
        val end = "not empty"

        emptyDirectionsGuard.onConfirm(start, end)
        Mockito.verify(wrapped).onConfirm(start, end)
    }
}
