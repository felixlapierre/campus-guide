package com.example.campusguide.utils

import android.content.Context
import com.android.volley.RequestQueue
import com.example.campusguide.directions.EmptyDirectionsGuard
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class RequestQueueTest {
    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testMaxOneDispatcher() {
        val obj1 = RequestDispatcher.getInstance(context)
        val obj2 = RequestDispatcher.getInstance(context);

        assert(obj1 == obj2)
    }
}