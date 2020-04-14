package com.example.campusguide.directions

import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class CallbackDirectionsConfirmTest {

    @Test
    fun testCallbackInvoked() {
        val callback: (String, String) -> Unit = mock()
        val start = "someStart"
        val end = "someEnd"
        whenever(callback(start, end)).thenReturn(null)

        val listener = CallbackDirectionsConfirmListener(callback)
        listener.onConfirm(start, end)
        verify(callback, atLeastOnce()).invoke(start, end)
    }

    @After
    fun validate() {
        Mockito.validateMockitoUsage()
    }
}
