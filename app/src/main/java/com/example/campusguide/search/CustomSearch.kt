package com.example.campusguide.search

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.campusguide.ActivityResultListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomSearch constructor(
    private val activity: Activity,
    private val locationProvider: SearchLocationProvider,
    private val locationListener: SearchLocationListener,
    private val requestCode: Int
) : View.OnClickListener,
    ActivityResultListener {
    override fun onClick(v: View?) {
        openCustomSearchActivity()
    }

    fun openCustomSearchActivity() {
        val searchIntent = Intent(activity, CustomSearchActivity::class.java)
        activity.startActivityForResult(searchIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != requestCode || resultCode != Activity.RESULT_OK) {
            return
        }

        val id = data?.data?.toString()
        if (id != null) {
            GlobalScope.launch {
                val location = locationProvider.getLocation(id)
                locationListener.onLocation(location)
            }
        }
    }
}