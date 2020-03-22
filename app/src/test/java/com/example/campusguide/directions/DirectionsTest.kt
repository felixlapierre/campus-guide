package com.example.campusguide.directions

import com.example.campusguide.Constants
import com.example.campusguide.utils.ErrorListener
import com.example.campusguide.utils.request.RequestDispatcher
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.URLEncoder

@RunWith(JUnit4::class)
class DirectionsTest {
    private val requestDispatcher: RequestDispatcher = mock()
    private val responseParser: GoogleDirectionsAPIResponseParser = mock()
    private val errorListener: ErrorListener = mock()

    private val jsonResponse: JSONObject = mock()

    @Test
    fun testDirectionsParsingFailed() {
        runBlocking {
            val start = "Concordia Hall Building"
            val end = "Concordia Faubourg Building"

            val startEncoded = URLEncoder.encode(start, "UTF-8")
            val endEncoded = URLEncoder.encode(end, "UTF-8")

            val expectedUrl = Constants.DIRECTIONS_API_URL + "?origin=$startEncoded&destination=$endEncoded"

            whenever(requestDispatcher.sendRequest(expectedUrl)).thenReturn(jsonResponse)
            whenever(responseParser.parse(jsonResponse)).thenReturn(null)

            val directions = Directions(requestDispatcher, responseParser, errorListener)

            val response = directions.getDirections(start, end, "test")

            assert(response == null)
            verify(errorListener).onError(Constants.DIRECTIONS_API_NULL_RESPONSE)
        }
    }

    @Test
    fun testDirectionsValidResponse() {
        runBlocking {
            val start = "Concordia Hall Building"
            val end = "Concordia Faubourg Building"

            val startEncoded = URLEncoder.encode(start, "UTF-8")
            val endEncoded = URLEncoder.encode(end, "UTF-8")

            val expectedUrl = Constants.DIRECTIONS_API_URL + "?origin=$startEncoded&destination=$endEncoded"

            val googleApiResponse = GoogleDirectionsAPIResponse("status", emptyList(), emptyList())
            whenever(requestDispatcher.sendRequest(expectedUrl)).thenReturn(jsonResponse)
            whenever(responseParser.parse(jsonResponse)).thenReturn(googleApiResponse)

            val directions = Directions(requestDispatcher, responseParser, errorListener)

            val response = directions.getDirections(start, end, "test")

            assert(response == googleApiResponse)
            verify(errorListener, never()).onError(any())
        }
    }
}