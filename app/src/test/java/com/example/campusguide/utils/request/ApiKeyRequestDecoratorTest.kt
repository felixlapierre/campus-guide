package com.example.campusguide.utils.request

import android.app.Activity
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
            val activity: Activity = mock()
            val wrapped: RequestDispatcher = mock()
            val response: JSONObject = mock()
            val apiKey = "key"
            val url = "url"
            val expectedUrl = "$url&key=$apiKey"

            whenever(wrapped.sendRequest(any())).thenReturn(response)

            val decorator = ApiKeyRequestDecorator(activity, wrapped)

            val returned = decorator.sendRequest(url)

            assert(returned == response)
        }
    }
}