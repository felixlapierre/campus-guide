package com.example.campusguide.search

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.Constants
import com.example.campusguide.MapsActivity
import com.example.campusguide.map.Map
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

const val AUTOCOMPLETE_REQUEST_CODE = 69; //nice

class CustomSearch constructor(private val activity: MapsActivity, private val map: Map) : View.OnClickListener,
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

        val result = data?.data?.toString()
        if (result != null) {
            getPlace(result)
        }
    }

    private fun getPlace(placeId: String) {
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

        val request = FetchPlaceRequest.newInstance(placeId, fields)

        Places.createClient(activity).fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            val location = place.latLng
            if(location != null) {
                marker?.remove()
                marker = map.addMarker(location, place.name!!)
                map.animateCamera(location, Constants.ZOOM_STREET_LVL)
            }
        }.addOnFailureListener { exception ->
            val message = exception.message
            if(message != null) {
                DisplayMessageErrorListener(activity).onError(message)
            }
        }
    }
}