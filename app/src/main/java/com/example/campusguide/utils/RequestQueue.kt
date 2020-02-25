package com.example.campusguide.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class RequestDispatcher constructor(context: Context){
    companion object {
        @Volatile
        private var INSTANCE: RequestDispatcher? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RequestDispatcher(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> sendRequest(req: Request<T>) {
        requestQueue.add(req)
    }
}