package com.example.campusguide

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.campusguide.directions.Directions
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DirectionsExploratoryTest {
    @Test
    fun testDirections() {
        runBlocking {
            val context = getInstrumentation().targetContext

            val response = Directions(context)
                .getDirections("Concordia Hall Building", "Faubourg Building")
            val eq = response?.status.equals("OK")
            assertThat(eq, equalTo(true))
        }
    }
}