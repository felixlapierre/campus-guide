package com.example.campusguide.directions

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.utils.MessageDialogFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.internal.PolylineEncoding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.MarkerOptions


/**
 * Represents a route between two coordinates on the map.
 * @param map The google map object on which the route will be displayed
 * @param ctx The context of the activity that contains the map
 * @param start The name of the location where the route starts
 * @param end The name of the location where the route ends
 */
class Route constructor(private val map: GoogleMap, private val activity: AppCompatActivity) {
    private var polyline: Polyline? = null

    fun set(start: String, end: String) {
        polyline?.remove()
        val directions = Directions(activity)

        //Create a coroutine so we can invoke the suspend function Directions::getDirections
        GlobalScope.launch {
            val apiKey = activity.resources.getString(com.example.campusguide.R.string.google_maps_key)
            val response = directions.getDirections(start, end)
            if(response != null) {
                response.routes[0]
                val startLat = response.routes[0].legs[0].startLocation.lat
                val startLng = response.routes[0].legs[0].startLocation.lng
                val endLat = response.routes[0].legs[0].endLocation.lat
                val endLng = response.routes[0].legs[0].endLocation.lng
                val start = MarkerOptions().position(LatLng(startLat.toDouble(), startLng.toDouble())).title("Start")
                val dest = MarkerOptions().position(LatLng(endLat.toDouble(), endLng.toDouble())).title("Destination");
                val line = response.routes[0].overviewPolyline.points
                val decoded = PolylineEncoding.decode(line)

                /**
                 * The com.google package contains two different representations of the
                 * LatLng class. The decoded points must be converted to the representation
                 * in com.google.android for PolylineOptions to accept them.
                 */
                val decodedAsGoodLatLng = decoded.map {
                    LatLng(it.lat, it.lng)
                }

                /**
                 * addPolyline throws an exception if it is not run on the Ui thread.
                 */
                activity.runOnUiThread {
                    polyline = map.addPolyline(PolylineOptions().addAll(decodedAsGoodLatLng))
                    map.addMarker(start)
                    map.addMarker(dest)
                }
            }
        }
    }
}