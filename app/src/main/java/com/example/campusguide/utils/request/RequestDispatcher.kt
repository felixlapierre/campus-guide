package com.example.campusguide.utils.request

import org.json.JSONObject

interface RequestDispatcher {
    suspend fun sendRequest(url: String): JSONObject
}