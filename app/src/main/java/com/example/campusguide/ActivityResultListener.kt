package com.example.campusguide

import android.content.Intent

interface ActivityResultListener {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
