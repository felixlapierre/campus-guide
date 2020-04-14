package com.example.campusguide.directions.outdoor

import com.example.campusguide.Constants
import com.example.campusguide.directions.GoogleDirectionsAPIResponse
import com.example.campusguide.directions.GoogleDirectionsAPIResponseParser
import com.example.campusguide.utils.ErrorListener
import com.example.campusguide.utils.request.RequestDispatcher
import java.net.URLEncoder

class OutdoorDirections constructor(
    private var requestDispatcher: RequestDispatcher,
    private var responseParser: GoogleDirectionsAPIResponseParser,
    private var errorListener: ErrorListener
) {
    /**
     * Gets directions from the Google API
     */
    suspend fun getDirections(
        startLocation: String,
        endLocation: String,
        travelMode: String,
        transitPreference: String?
    ): GoogleDirectionsAPIResponse? {
        /**
         * Start, end and travel mode will be placed in query params, so they must be urlEncoded.
         */
        val startEncoded = URLEncoder.encode(startLocation, "UTF-8")
        val endEncoded = URLEncoder.encode(endLocation, "UTF-8")
        val travelModeEncoded = URLEncoder.encode(travelMode.toLowerCase(), "UTF-8")

        val path = Constants.DIRECTIONS_API_URL
        var url = "$path?origin=$startEncoded&destination=$endEncoded&mode=$travelModeEncoded"
        if(transitPreference != null) {
            val transitPreferenceEncoded = URLEncoder.encode(transitPreference, "UTF-8")
            url = "$url&transit_routing_preference=$transitPreferenceEncoded"
        }

        val response = requestDispatcher.sendRequest(url)
        val responseObj = responseParser.parse(response)

        if (responseObj == null) {
            errorListener.onError(Constants.DIRECTIONS_API_NULL_RESPONSE)
        }

        return responseObj
    }
}
