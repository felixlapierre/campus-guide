package com.example.campusguide.utils.request

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.campusguide.Constants
import com.example.campusguide.utils.ErrorListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.json.JSONObject

class VolleyRequestDispatcher constructor(val context: Context, val errorListener: ErrorListener) :
    RequestDispatcher {

    override suspend fun sendRequest(url: String) = suspendCoroutine<JSONObject> { cont ->
        val request = JsonObjectRequest(
            Request.Method.GET, "$url\n", null,
            Response.Listener<JSONObject> { response ->
                if (response.getString("status") == "REQUEST_DENIED") {
                    errorListener.onError(Constants.REQUEST_DENIED_MSG)
                } else {
                    cont.resume(response)
                }
            },
            Response.ErrorListener { error ->
                error.message?.let { errorListener.onError(it) }
            }
        )

        RequestQueueSingleton.getInstance(context).sendRequest(request)
    }
}
