package com.example.campusguide.directions

import android.graphics.Color
import com.example.campusguide.Constants
import com.example.campusguide.utils.Helper
import com.example.campusguide.map.Map
import com.example.campusguide.map.Marker
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PathPolyline constructor(startName: String, endName: String, val segment: Segment) {
    class PolylineStyle {
        private val patternDash: PatternItem = Dash(Constants.PATTERN_DASH_LENGTH_PX)
        private val patternGap: PatternItem = Gap(Constants.PATTERN_GAP_LENGTH_PX)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val pathColor = Color.parseColor(Constants.ACCENT_COLOR)
    }

    private lateinit var path: List<LatLng>
    private val polylineOptions: PolylineOptions
    private var polyline: Polyline? = null

    private val startMarkerOptions: MarkerOptions
    private var startMarker: Marker? = null
    private val endMarkerOptions: MarkerOptions
    private var endMarker: Marker? = null

    private val deferred: Deferred<Unit>

    init {
        val style = PolylineStyle()
        polylineOptions = PolylineOptions()
        startMarkerOptions = MarkerOptions()
        endMarkerOptions = MarkerOptions()

        deferred = GlobalScope.async {
            path = segment.toListOfCoordinates()
            polylineOptions.addAll(path)
                .color(style.pathColor)
                .pattern(style.patternPolygonAlpha)

            val firstPoint = path[0]
            startMarkerOptions.position(firstPoint)
                .title(Helper.capitalizeWords(startName))
                .snippet("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

            val lastPoint = path[path.size - 1]
            endMarkerOptions.position(lastPoint)
                .title(Helper.capitalizeWords(endName))
                .snippet("Destination")
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

    fun getPathBounds() : LatLngBounds {
        var north: Double = path[0].latitude
        var south: Double = path[0].latitude
        var east: Double = path[0].longitude
        var west: Double = path[0].longitude

        path.forEach {point ->
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