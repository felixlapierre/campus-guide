package com.example.campusguide.utils

import androidx.fragment.app.FragmentActivity

class DisplayMessageErrorListener constructor(private val activity: FragmentActivity): ErrorListener {
    override fun onError(message: String) {
        MessageDialogFragment(message).show(activity.supportFragmentManager, "errorMessage")
    }
}