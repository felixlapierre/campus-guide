package com.example.campusguide.directions

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.beust.klaxon.Klaxon
import com.example.campusguide.R
import com.example.campusguide.utils.MessageDialogFragment
import com.example.campusguide.utils.RequestDispatcher
import org.json.JSONObject
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Directions constructor(private var activity: AppCompatActivity){
    /**
     * Gets directions from the Google API
     */
    suspend fun getDirections(startLocation: String, endLocation: String) = suspendCoroutine<GoogleDirectionsAPIResponse?> { cont ->
        val apiKey = activity.resources.getString(R.string.google_maps_key)

        /**
         * Start and end will be placed in query params, so they must be urlEncoded.
         */
        val startEncoded = URLEncoder.encode(startLocation, "UTF-8")
        val endEncoded = URLEncoder.encode(endLocation, "UTF-8")

        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$startEncoded&destination=$endEncoded&key=$apiKey\n"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                val responseObj = Klaxon().parse<GoogleDirectionsAPIResponse>(response.toString(0))

                if(responseObj == null) {
                    MessageDialogFragment("Could not get a response from the Google Directions API")
                        .show(activity.supportFragmentManager, "message")
                } else if(responseObj.status == "REQUEST_DENIED") {
                    MessageDialogFragment(
                        "Request to Google Directions API was denied. Make sure the" +
                                " API key has permissions to access the Google Directions API."
                    )
                        .show(activity.supportFragmentManager, "message")
                }

                cont.resume(responseObj)
            },
            Response.ErrorListener { println("Error sending request") }
        )

        RequestDispatcher.getInstance(activity).sendRequest(request)
    }
}