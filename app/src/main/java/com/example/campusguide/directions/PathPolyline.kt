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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PathPolyline private constructor(val startName: String, val endName: String, private val deferred: Deferred<List<LatLng>>) {
    class PolylineStyle {
        private val patternDashLengthPx = 20.0f
        private val patternGapLengthPx = 20.0f
        private val patternDash: PatternItem = Dash(patternDashLengthPx)
        private val patternGap: PatternItem = Gap(patternGapLengthPx)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val colorBlueArgb = 0xff0000ff
    }

    private lateinit var path: List<LatLng>
    private var stepsPath: RoutePreviewData = RoutePreviewData()
    private var polylineOptions: PolylineOptions
    private var polyline: Polyline? = null

    private var startMarkerOptions: MarkerOptions
    private var startMarker: Marker? = null
    private var endMarkerOptions: MarkerOptions
    private var endMarker: Marker? = null
    private lateinit var segment: Segment

    constructor(startName: String, endName: String, segment: Segment) : this(
        startName,
        endName,
        GlobalScope.async {
            segment.toListOfCoordinates()
        }){
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
            .color(style.colorBlueArgb.toInt())
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

        stepsPath.setPath(path)
        if(this::segment.isInitialized)
            stepsPath.setSteps(segment.getSteps())
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

    fun getSteps(): RoutePreviewData {
        return stepsPath
    }

    fun getDuration() : Int {
        return segment.getDuration()
    }

    fun getDistance() : String{
        return segment.getDistance()
    }
}