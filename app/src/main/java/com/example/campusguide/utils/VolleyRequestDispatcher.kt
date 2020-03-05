package com.example.campusguide.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.json.JSONObject

class VolleyRequest constructor(val context: Context, val errorListener: ErrorListener) {
    val requestDeniedErrorMessage = "Request to Google Directions API was denied. Make sure the" +
            " API key has permissions to access the Google Directions API."

    suspend fun send(url: String) = suspendCoroutine<JSONObject>{ cont ->
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                if(response.getString("status") == "REQUEST_DENIED") {
                    errorListener.onError(requestDeniedErrorMessage)
                } else {
                    cont.resume(response)
                }
            },
            Response.ErrorListener { error ->
                error.message?.let {errorListener.onError(it)}
            }
        )

        RequestQueueSingleton.getInstance(context).sendRequest(request)
    }
}