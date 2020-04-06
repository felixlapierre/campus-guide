package com.example.campusguide.location

import android.app.Activity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class FusedLocationProvider constructor(private val activity: Activity) : LocationProvider {
    private var fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)

    override fun getLocation(callback: (Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener(activity) { androidLocation ->
            if (androidLocation != null) {
                val location = CurrentLocation(androidLocation.latitude, androidLocation.longitude)
                callback(location)
            }
        }
    }

}