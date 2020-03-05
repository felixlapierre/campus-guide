package com.example.campusguide.directions

import com.example.campusguide.Constants
import com.example.campusguide.utils.ErrorListener
import com.example.campusguide.utils.RequestDispatcher
import java.net.URLEncoder

class Directions constructor(
    private var requestDispatcher: RequestDispatcher,
    private var responseParser: GoogleDirectionsAPIResponseParser,
    private var errorListener: ErrorListener
) {
    /**
     * Gets directions from the Google API
     */
    suspend fun getDirections(
        startLocation: String,
        endLocation: String
    ): GoogleDirectionsAPIResponse? {
        /**
         * Start and end will be placed in query params, so they must be urlEncoded.
         */
        val startEncoded = URLEncoder.encode(startLocation, "UTF-8")
        val endEncoded = URLEncoder.encode(endLocation, "UTF-8")

        val path = Constants.DIRECTIONS_API_URL
        val url =
            "$path?origin=$startEncoded&destination=$endEncoded"

        val response = requestDispatcher.sendRequest(url)
        val responseObj = responseParser.parse(response)

        if (responseObj == null) {
            errorListener.onError(Constants.DIRECTIONS_API_NULL_RESPONSE)
        }

        return responseObj
    }
}