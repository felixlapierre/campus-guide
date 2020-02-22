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

/**
 * Represents a route between two coordinates on the map.
 * @param map The google map object on which the route will be displayed
 * @param ctx The context of the activity that contains the map
 * @param start The name of the location where the route starts
 * @param end The name of the location where the route ends
 */
class Route constructor(private val map: GoogleMap, ctx: AppCompatActivity, start: String, end: String) {
    private lateinit var polyline: Polyline

    //This is the body of the primary constructor which was defined on the class declaration line
    init {
        val directions = Directions(ctx)

        //Create a coroutine so we can invoke the suspend function Directions::getDirections
        GlobalScope.launch {
            val response = directions.getDirections(start, end)

            if(response != null) {
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
                ctx.runOnUiThread {
                    map.addPolyline(PolylineOptions().addAll(decodedAsGoodLatLng))
                }
            }
        }
    }

    fun remove() {
        polyline.remove()
    }
}