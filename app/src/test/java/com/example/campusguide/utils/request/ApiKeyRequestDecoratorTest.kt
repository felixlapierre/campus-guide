package com.example.campusguide.utils.request

import android.app.Activity
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ApiKeyRequestDecoratorTest {

    @Test
    fun testDecorateWithApiKey() {
        runBlocking {
            // Mock all dependencies
            val activity: Activity = mock()
            val resources: Resources = mock()
            val wrapped: RequestDispatcher = mock()
            val response: JSONObject = mock()

            // Determine what the expected decorated url will be
            val apiKey = "someApiKey"
            val url = "url"
            val expectedUrl = "$url&key=$apiKey"

            // Set up mocks to provide fake api key
            whenever(activity.resources).thenReturn(resources)
            whenever(resources.getString(any())).thenReturn(apiKey)

            whenever(wrapped.sendRequest(expectedUrl)).thenReturn(response)

            val decorator = ApiKeyRequestDecorator(activity, wrapped)
            val returned = decorator.sendRequest(url)

            assert(returned == response)
        }
    }
}