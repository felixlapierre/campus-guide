package com.example.campusguide.directions

import androidx.appcompat.app.AppCompatActivity
import com.example.campusguide.Constants
import com.example.campusguide.utils.ApiKeyRequestDecorator
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.VolleyRequestDispatcher
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.internal.PolylineEncoding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds

/**
 * Represents a route between two coordinates on the map.
 * @param map The google map object on which the route will be displayed
 * @param ctx The context of the activity that contains the map
 * @param start The name of the location where the route starts
 * @param end The name of the location where the route ends
 */
class Route constructor(private val map: GoogleMap, private val activity: AppCompatActivity) {
    private val PATTERN_DASH_LENGTH_PX = 20
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val PATTERN_POLYGON_ALPHA = listOf(GAP, DASH)
    private val COLOR_BLUE_ARGB = 0xff0000ff
    private var polyline: Polyline? = null
    private var begin: Marker? = null
    private var dest: Marker? = null
    private val startString: String = "Start"
    private val destString: String = "Destination"

    private fun capitalizeWords(location: String): String
    {
        val words = location.split(" ").toMutableList()
        var output = ""
        for (word in words) {
            output += word.capitalize() + " "
        }
        output = output.trim()
        return output
    }

    fun set(start: String, end: String) {
        polyline?.remove()
        begin?.remove()
        dest?.remove()
        val errorListener = DisplayMessageErrorListener(activity);
        val directions = Directions(
            ApiKeyRequestDecorator(activity,
                VolleyRequestDispatcher(activity, errorListener)),
            KlaxonDirectionsAPIResponseParser(),
            errorListener)

        //Create a coroutine so we can invoke the suspend function Directions::getDirections
        GlobalScope.launch {
            val response = directions.getDirections(start, end)
            if (response != null) {
                val startPoint = MarkerOptions().position(
                    LatLng(
                        response.routes[0].legs[0].startLocation.lat.toDouble(),
                        response.routes[0].legs[0].startLocation.lng.toDouble()
                    )
                ).title(capitalizeWords(start)).snippet(startString)
                val endPoint = MarkerOptions().position(
                    LatLng(
                        response.routes[0].legs[0].endLocation.lat.toDouble(),
                        response.routes[0].legs[0].endLocation.lng.toDouble()
                    )
                ).title(capitalizeWords(end)).snippet(destString)
                val routeBounds = LatLngBounds(
                    LatLng(
                        response.routes[0].bounds.southwest.lat.toDouble(),
                        response.routes[0].bounds.southwest.lng.toDouble()
                    ),
                    LatLng(
                        response.routes[0].bounds.northeast.lat.toDouble(),
                        response.routes[0].bounds.northeast.lng.toDouble()
                    )
                )
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
                    val polyOptions = PolylineOptions()
                        .color(COLOR_BLUE_ARGB.toInt())
                        .pattern(PATTERN_POLYGON_ALPHA)
                        .addAll(decodedAsGoodLatLng)
                    polyline = map.addPolyline(polyOptions)
                    begin = map.addMarker(startPoint)
                    dest = map.addMarker(endPoint)
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            routeBounds.center,
                            Constants.ZOOM_STREET_LVL
                        )
                    )
                }
            }
        }
    }

    private fun decodeLine(line: String): List<LatLng>{
        /**
         * The com.google package contains two different representations of the
         * LatLng class. The decoded points must be converted to the representation
         * in com.google.android for PolylineOptions to accept them.
         */
        val decoded = PolylineEncoding.decode(line)
        return decoded.map {
            LatLng(it.lat, it.lng)
        }
    }

    private fun runAddPolyline(decodedAsGoodLatLng: List<LatLng>){
        /**
         * addPolyline throws an exception if it is not run on the Ui thread.
         */
        activity.runOnUiThread {
            polyline = map.addPolyline(
                PolylineOptions().addAll(
                    decodedAsGoodLatLng))
        }
    }
}