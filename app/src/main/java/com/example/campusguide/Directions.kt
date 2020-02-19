package com.example.campusguide

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.campusguide.utils.RequestDispatcher
import org.json.JSONObject

class Directions {
    fun getDirections(startLocation: String, endLocation: String, ctx: Context) {
        val url = "test"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->

            },
            Response.ErrorListener { println("Error sending request") }
        )

        RequestDispatcher.getInstance(ctx).sendRequest(request);
    }
}