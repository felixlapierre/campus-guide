package com.example.campusguide.directions

import android.graphics.Color
import com.example.campusguide.Constants
import com.example.campusguide.map.Map
import com.example.campusguide.map.IMarker
import com.example.campusguide.utils.Helper
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PathPolyline private constructor(val startName: String, val endName: String, private val deferred: Deferred<List<LatLng>>) {
    class PolylineStyle {
        private val patternDash: PatternItem = Dash(Constants.PATTERN_DASH_LENGTH_PX)
        private val patternGap: PatternItem = Gap(Constants.PATTERN_GAP_LENGTH_PX)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val pathColor = Color.parseColor(Constants.AZURE_COLOR)
    }

    private lateinit var path: List<LatLng>
    private var routePreviewData: RoutePreviewData = RoutePreviewData()
    private var polylineOptions: PolylineOptions
    private var polyline: Polyline? = null

    private var startMarkerOptions: MarkerOptions
    private var startMarker: IMarker? = null
    private var endMarkerOptions: MarkerOptions
    private var endMarker: IMarker? = null
    private lateinit var segment: Segment

    constructor(startName: String, endName: String, segment: Segment) : this(
        startName,
        endName,
        GlobalScope.async {
            segment.toListOfCoordinates()
        }) {
        this.segment = segment
    }

    constructor(startName: String, endName: String, line: List<LatLng>) : this(
        startName,
        endName,
        CompletableDeferred(line)
    )

    init {
        polylineOptions = PolylineOptions()
        startMarkerOptions = MarkerOptions()
        endMarkerOptions = MarkerOptions()
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
        path = deferred.await()

        val style = PolylineStyle()

        polylineOptions = PolylineOptions()
        polylineOptions.addAll(path)
            .color(style.pathColor)
            .pattern(style.patternPolygonAlpha)

        val firstPoint = path[0]
        startMarkerOptions = MarkerOptions()
        startMarkerOptions.position(firstPoint)
            .title(Helper.capitalizeWords(startName))
            .snippet("Start")

        val lastPoint = path[path.size - 1]
        endMarkerOptions = MarkerOptions()
        endMarkerOptions.position(lastPoint).title(Helper.capitalizeWords(endName))
            .snippet("Destination")

        routePreviewData.setPath(path)
        if (this::segment.isInitialized)
            routePreviewData.setSteps(segment.getSteps())
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

    fun getSteps(): List<GoogleDirectionsAPIStep> {
        return segment.getSteps()
    }

    fun getDuration(): Int {
        return segment.getDuration()
    }

    fun getDistance(): String {
        return segment.getDistance()
    }

    fun getFare(): String {
        return segment.getFare()
    }

    fun getRoutePreviewData(): RoutePreviewData {
        return routePreviewData
    }
}
