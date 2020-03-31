package com.example.campusguide.directions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.DirectionsActivity
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.utils.permissions.Permissions
import com.google.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DirectionsFlow constructor(private val activity: AppCompatActivity, private val permissions: Permissions, private val locationProvider: FusedLocationProvider) {
    fun startFlow(origin: String?, destination: String?) {
        GlobalScope.launch {
            val finalDestination = destination ?: getDestination()
            val finalOrigin = origin ?: getOrigin()

            val intent = Intent(activity, DirectionsActivity::class.java).apply {
                putExtra("Origin", finalOrigin)
                putExtra("Destination", finalDestination)
            }
            activity.startActivity(intent)
        }
    }

    private suspend fun getDestination() = suspendCoroutine<String> { cont ->
        val chooseDestinationOptions = ChooseDestinationOptions { destination ->
            val destinationLatLng = LatLng(destination.lat, destination.lon)
            cont.resume(destinationLatLng.toString())
        }
        chooseDestinationOptions.show(
            activity.supportFragmentManager,
            "chooseDestinationOptions"
        )
    }

    private suspend fun getOrigin() = suspendCoroutine<String> { cont ->
        val chooseOriginOptions = ChooseOriginOptions(permissions, locationProvider) { origin ->
            val originLatLng = LatLng(origin.lat, origin.lon)
            cont.resume(originLatLng.toString())
        }
        chooseOriginOptions.show(activity.supportFragmentManager, "chooseOriginOptions")
    }
}