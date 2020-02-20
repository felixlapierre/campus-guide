package com.example.campusguide

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.campusguide.utils.RequestDispatcher
import org.json.JSONObject
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Directions {
    private var context: Context
    constructor(applicationContext: Context) {
        context = applicationContext
    }
    suspend fun getDirections(startLocation: String, endLocation: String) = suspendCoroutine<JSONObject> { cont ->
        val apiKey = context.resources.getString(R.string.google_maps_key)
        val startEncoded = URLEncoder.encode(startLocation, "UTF-8")
        val endEncoded = URLEncoder.encode(endLocation, "UTF-8")

        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$startEncoded&destination=$endEncoded&key=$apiKey\n"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                cont.resume(response)
            },
            Response.ErrorListener { println("Error sending request") }
        )

        RequestDispatcher.getInstance(context).sendRequest(request)
    }
}