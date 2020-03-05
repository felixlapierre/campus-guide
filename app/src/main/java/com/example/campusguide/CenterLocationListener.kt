package com.example.campusguide

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CenterLocationListener constructor(private val activity: Activity, private val map: Map): View.OnClickListener {
    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

    override fun onClick(v: View?) {
        //Check if location permission has been granted
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            goToCurrentLocation()
        } else {
            //Request location permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_ACCESS_CODE
            )
        }
    }

    /**
     * Centers the map on the user's current location and places a marker.
     */
    private fun goToCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(activity) { location ->
            if(location != null) {
                animateCurrentLocation(location)
            }
        }
    }

    private fun animateCurrentLocation(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        map.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .title("You are here.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentLatLng,
                Constants.ZOOM_STREET_LVL
            )
        )
    }

}