package com.example.campusguide.directions

import com.example.campusguide.map.Map
import com.example.campusguide.map.Marker
import com.example.campusguide.utils.Helper
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

class PathPolyline constructor(startName: String, endName: String, val segment: Segment) {
    class PolylineStyle {
        private val patternDashLengthPx = 20.0f
        private val patternGapLengthPx = 20.0f
        private val patternDash: PatternItem = Dash(patternDashLengthPx)
        private val patternGap: PatternItem = Gap(patternGapLengthPx)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val colorBlueArgb = 0xff0000ff
    }

    private lateinit var path: List<LatLng>
    private var stepsPath: Test = Test()
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
                .color(style.colorBlueArgb.toInt())
                .pattern(style.patternPolygonAlpha)

            stepsPath.setSteps(segment.getSteps())

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

    fun getSteps(): Test {
        return  stepsPath
    }
}
