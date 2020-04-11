package com.example.campusguide.utils

import android.app.Application
import android.content.Context

class LocalResources : Application() {

    companion object {
        private lateinit var mContext: Context

        fun getContext(): Context {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}
