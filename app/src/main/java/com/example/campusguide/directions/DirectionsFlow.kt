package com.example.campusguide.directions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.DirectionsActivity
import com.example.campusguide.location.FusedLocationProvider
import com.example.campusguide.location.Location
import com.example.campusguide.search.amenities.AmenitiesLocationProvider
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.example.campusguide.search.indoor.IndoorLocationProvider
import com.example.campusguide.search.outdoor.PlacesApiSearchLocationProvider
import com.example.campusguide.utils.permissions.Permissions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class DirectionsFlow constructor(
    private val activity: AppCompatActivity,
    private val permissions: Permissions,
    private val locationProvider: FusedLocationProvider
) {
    fun startFlow(origin: Location? = null, destination: Location? = null) {
        GlobalScope.launch {
            val finalDestination = destination ?: getDestination()
            val finalOrigin = origin ?: getOrigin()

            val intent = Intent(activity, DirectionsActivity::class.java).apply {
                putExtra("OriginName", finalOrigin.name)
                putExtra("OriginEncoded", finalOrigin.encodeForDirections())
                putExtra("DestinationName", finalDestination.name)
                putExtra("DestinationEncoded", finalDestination.encodeForDirections())
            }
            activity.startActivity(intent)
            println("After start activity")
        }
    }

    private suspend fun getDestination() = suspendCoroutine<Location> { cont ->
        val chooseDestinationOptions = ChooseDestinationOptions(
            IndoorLocationProvider(
            BuildingIndexSingleton.getInstance(activity.assets),
            AmenitiesLocationProvider(
                PlacesApiSearchLocationProvider(activity),
                permissions,
                activity,
                locationProvider
            )
        )) { destination ->
            cont.resume(destination)
        }
        chooseDestinationOptions.show(
            activity.supportFragmentManager,
            "chooseDestinationOptions"
        )
    }

    private suspend fun getOrigin() = suspendCoroutine<Location> { cont ->
        val chooseOriginOptions = ChooseOriginOptions(permissions, locationProvider) { origin ->
            cont.resume(origin)
        }
        chooseOriginOptions.show(activity.supportFragmentManager, "chooseOriginOptions")
    }
}
