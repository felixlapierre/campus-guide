package com.example.campusguide.search

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.MapsActivity
import com.example.campusguide.map.Map
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val AUTOCOMPLETE_REQUEST_CODE = 69; //nice

class CustomSearch constructor(
    private val activity: MapsActivity,
    private val locationProvider: SearchLocationProvider,
    private val locationListener: SearchLocationListener
) : View.OnClickListener,
    ActivityResultListener {
    override fun onClick(v: View?) {
        val searchIntent = Intent(activity, CustomSearchActivity::class.java)
        activity.startActivityForResult(searchIntent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != AUTOCOMPLETE_REQUEST_CODE || resultCode != Activity.RESULT_OK) {
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