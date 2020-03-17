package com.example.campusguide.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.ActivityResultListener
import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.map.Map
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class Search constructor(private val activity: FragmentActivity, private val map: Map)
    : ConnectionCallbacks,
    OnConnectionFailedListener,
    View.OnClickListener,
    ActivityResultListener{
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest

    init {
        if (!Places.isInitialized())
            Places.initialize(activity.applicationContext, activity.getString(R.string.google_maps_key))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
            }
        } else {
            buildGoogleApiClient()
        }
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(activity)
        }
    }

    override fun onConnectionSuspended(i: Int) {
        print("Connection is SUSPENDED")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        print("Connection has FAILED")
    }

    override fun onClick(view: View) {
        val fields =
            arrayListOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        val intent: Intent =
            Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(activity)
        activity.startActivityForResult(intent, Constants.AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != Constants.AUTOCOMPLETE_REQUEST_CODE) { return }

        when (resultCode) {
            AppCompatActivity.RESULT_OK -> {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                val location = place.latLng!!
                map.addMarker(location, place.name!!)
                map.animateCamera(location, Constants.ZOOM_STREET_LVL)
                // TODO: Restore feature to change "campus name" to target of search
                // campus_name.text = place.name
            }
            AutocompleteActivity.RESULT_ERROR -> {
                print("The Request has run into an error")
                print(data)
            }
            AppCompatActivity.RESULT_CANCELED -> {
                print("The Request was cancelled")
                print(data)
            }
        }
    }
}