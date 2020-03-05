package com.example.campusguide.utils

import org.json.JSONObject

interface RequestDispatcher {
    suspend fun sendRequest(url: String): JSONObject
}