package com.example.campusguide.search

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.map.Map
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val AUTOCOMPLETE_REQUEST_CODE = 69; //nice

class CustomSearch constructor(
    private val activity: MapsActivity,
    private val map: Map,
    private val locationProvider: SearchLocationProvider
) : View.OnClickListener,
    ActivityResultListener {

    private var marker: Marker? = null

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
            getLocationFromId(id)
        }
    }

    private fun getLocationFromId(locationId: String) {
        GlobalScope.launch {
            val searchLocation = locationProvider.getLocation(locationId)
            val latlng = LatLng(searchLocation.lat, searchLocation.lon)
            activity.runOnUiThread {
                marker?.remove()
                marker = map.addMarker(latlng, searchLocation.name)
                map.animateCamera(latlng, Constants.ZOOM_STREET_LVL)
            }
        }
    }
}