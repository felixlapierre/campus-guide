package com.example.campusguide.location

import android.app.Activity
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class FusedLocationProvider constructor(private val activity: Activity) : LocationProvider {
    private var fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

    override fun getLocation(callback: (Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener(activity) { location ->
            if (location != null) {
                callback(location)
            }
        }
    }

}