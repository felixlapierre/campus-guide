package com.example.campusguide.directions

import com.example.campusguide.utils.ErrorListener
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class EmptyDirectionsGuardTest() {

    @Mock lateinit var wrapped: DirectionsDialogConfirmationListener
    @Mock lateinit var errorListener: ErrorListener

    private lateinit var emptyDirectionsGuard: EmptyDirectionsGuard

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        emptyDirectionsGuard = EmptyDirectionsGuard(wrapped, errorListener)
    }

    @Test
    fun onConfirmCalled_WithDefinedStrings() {
        val start = "someStart"
        val end = "someEnd"

        emptyDirectionsGuard.onConfirm(start, end)
        Mockito.verify(wrapped).onConfirm(start, end)
    }

    @Test
    fun onConfirmCalled_WithEmptyStrings() {
        val start = "";
        val end = "";

        emptyDirectionsGuard.onConfirm(start, end)
        Mockito.verify(errorListener).onError(emptyDirectionsGuard.errorMessage)
    }
}
