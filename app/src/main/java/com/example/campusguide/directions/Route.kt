package com.example.campusguide.directions

import android.widget.RadioButton
import androidx.fragment.app.FragmentActivity
import com.example.campusguide.map.Map
import com.example.campusguide.utils.request.ApiKeyRequestDecorator
import com.example.campusguide.utils.DisplayMessageErrorListener
import com.example.campusguide.utils.request.VolleyRequestDispatcher
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.internal.PolylineEncoding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.model.TravelMode
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

/**
 * Represents a route between two coordinates on the map.
 * @param map The google map object on which the route will be displayed
 * @param ctx The context of the activity that contains the map
 * @param start The name of the location where the route starts
 * @param end The name of the location where the route ends
 */
class Route constructor(private val map: Map, private val activity: FragmentActivity) {
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

   fun set(start: String, end: String, travelMode: TravelMode, optionalCallback: (() -> Unit)? = null) {
        val travelModeString: String = when (travelMode){
            TravelMode.WALKING ->
                "Walking";
            TravelMode.DRIVING ->
                "Driving";
            TravelMode.TRANSIT ->
                "Transit";
            TravelMode.BICYCLING ->
                "Bicycling";
            TravelMode.UNKNOWN ->
                "Unknown";
        }
        this.set(start, end, travelModeString, optionalCallback);
    }

    // TODO: Make the Route.set method waay smaller by extracting functionality and moving responsibilities to proper classes
    fun set(start: String, end: String, travelMode: String, optionalCallback: (() -> Unit)? = null) {
        polyline?.remove()
        begin?.remove()
        dest?.remove()

        val errorListener = DisplayMessageErrorListener(activity);
        val directions = Directions(
            ApiKeyRequestDecorator(
                activity,
                VolleyRequestDispatcher(
                    activity,
                    errorListener
                )
            ),
            KlaxonDirectionsAPIResponseParser(),
            errorListener)

        //Create a coroutine so we can invoke the suspend function Directions::getDirections
        GlobalScope.launch {
            val response = directions.getDirections(start, end, travelMode)
            if (response != null) {

                val startPoint = LatLng(
                    response.routes[0].legs[0].startLocation.lat.toDouble(),
                    response.routes[0].legs[0].startLocation.lng.toDouble()
                )
                val startPointMarkerOptions = MarkerOptions().position(
                    startPoint
                ).title(capitalizeWords(start)).snippet(startString)

                val endPoint = LatLng(
                    response.routes[0].legs[0].endLocation.lat.toDouble(),
                    response.routes[0].legs[0].endLocation.lng.toDouble()
                )
                val endPointMarkerOptions = MarkerOptions().position(
                    endPoint
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
                    // Set the duration of the route
                    val radioButtonId = "radio_" + travelMode.toLowerCase()
                    val id = activity.resources.getIdentifier(radioButtonId, "id", activity.packageName)
                    activity.findViewById<RadioButton>(id).apply {
                        text = response.routes[0].legs[0].duration.text
                    }
                    val polyOptions = PolylineOptions()
                        .color(COLOR_BLUE_ARGB.toInt())
                        .pattern(PATTERN_POLYGON_ALPHA)
                        .addAll(decodedAsGoodLatLng)
                    polyline = map.addPolyline(polyOptions)
                    begin = map.addMarker(startPointMarkerOptions)
                    dest = map.addMarker(endPointMarkerOptions)
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            routeBounds,
                            100 // padding around the route in pixels
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