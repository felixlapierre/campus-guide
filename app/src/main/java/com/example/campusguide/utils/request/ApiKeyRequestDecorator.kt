package com.example.campusguide.utils.request

import android.app.Activity
import com.example.campusguide.R
import org.json.JSONObject

class ApiKeyRequestDecorator constructor(
    private val activity: Activity,
    private val wrapped: RequestDispatcher
) : RequestDispatcher {
    override suspend fun sendRequest(url: String): JSONObject {
        val key = activity.resources.getString(R.string.google_maps_key)
        return wrapped.sendRequest("$url&key=$key")
    }
}
