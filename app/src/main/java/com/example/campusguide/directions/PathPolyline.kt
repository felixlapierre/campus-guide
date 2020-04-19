package com.example.campusguide.directions

import android.graphics.Color
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.example.campusguide.map.Marker
import com.example.campusguide.utils.Helper
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PolylineStyle constructor(
    private val patternDash: PatternItem = Dash(Constants.PATTERN_DASH_LENGTH_PX),
    private val patternGap: PatternItem = Gap(Constants.PATTERN_GAP_LENGTH_PX),
    val pathColor:Int = Color.parseColor(Constants.AZURE_COLOR)
){
    val patternPolygonAlpha = listOf(patternGap, patternDash)
}

class PathPolyline constructor(
    startName: String,
    endName: String,
    val segment: Segment,
    private val style: PolylineStyle = PolylineStyle(),
    private val startMarkerOptions: MarkerOptions =
        MarkerOptions()
            .title(Helper.capitalizeWords(startName))
            .snippet("Start")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)),
    private val endMarkerOptions: MarkerOptions =
        MarkerOptions()
            .title(Helper.capitalizeWords(endName))
            .snippet("Destination")
) {


    private lateinit var path: List<LatLng>
    private val polylineOptions: PolylineOptions = PolylineOptions()
    private var polyline: Polyline? = null

    private var startMarker: Marker? = null
    private var endMarker: Marker? = null

    private val deferred: Deferred<Unit>

    init {

        deferred = GlobalScope.async {
            path = segment.toListOfCoordinates()
            polylineOptions.addAll(path)
                .color(style.pathColor)
                .pattern(style.patternPolygonAlpha)

            val firstPoint = path[0]
            startMarkerOptions.position(firstPoint)

            val lastPoint = path[path.size - 1]
            endMarkerOptions.position(lastPoint)

            Unit
        }
    }

    fun addToMap(map: Map) {
        polyline = map.addPolyline(polylineOptions)
        startMarker = map.addMarker(startMarkerOptions)
        endMarker = map.addMarker(endMarkerOptions)
    }

    fun removeFromMap() {
        polyline?.remove()
        startMarker?.remove()
        endMarker?.remove()
    }

    suspend fun waitUntilCreated() {
        deferred.await()
    }

    fun getPathBounds(): LatLngBounds {
        var north: Double = path[0].latitude
        var south: Double = path[0].latitude
        var east: Double = path[0].longitude
        var west: Double = path[0].longitude

        path.forEach { point ->
            north = Math.max(north, point.latitude)
            south = Math.min(south, point.latitude)
            east = Math.max(east, point.longitude)
            west = Math.min(west, point.longitude)
        }

        val southwest = LatLng(south, west)
        val northeast = LatLng(north, east)

        return LatLngBounds(southwest, northeast)
    }
}
