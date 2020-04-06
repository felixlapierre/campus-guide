package com.example.campusguide.directions

import com.example.campusguide.utils.Helper
import com.example.campusguide.map.Map
import com.example.campusguide.map.Marker
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PathPolyline constructor(startName: String, endName: String, val segment: Segment) {
    class PolylineStyle {
        private val patternDashLengthPx = 20.0f
        private val patternGapLengthPx = 20.0f
        private val patternDash: PatternItem = Dash(patternDashLengthPx)
        private val patternGap: PatternItem = Gap(patternGapLengthPx)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val colorBlueArgb = 0xff0000ff
    }

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
            val path = segment.toListOfCoordinates()
            polylineOptions.addAll(path)
                .color(style.colorBlueArgb.toInt())
                .pattern(style.patternPolygonAlpha)

            val firstPoint = path[0]
            startMarkerOptions.position(firstPoint)
                .title(Helper.capitalizeWords(startName))
                .snippet("Start")

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
}