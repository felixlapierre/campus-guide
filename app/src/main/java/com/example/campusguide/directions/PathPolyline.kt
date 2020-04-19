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

class PathPolyline constructor(startName: String, endName: String, val segment: Segment) {
    class PolylineStyle {
        private val patternDash: PatternItem = Dash(Constants.PATTERN_DASH_LENGTH_PX)
        private val patternGap: PatternItem = Gap(Constants.PATTERN_GAP_LENGTH_PX)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val pathColor = Color.parseColor(Constants.AZURE_COLOR)
    }

    private lateinit var paths: List<Path>
    private val polylineOptions: MutableList<PolylineOptions>
    private var polylines: MutableList<Polyline>

    private val startMarkerOptions: MarkerOptions
    private var startMarker: Marker? = null
    private val endMarkerOptions: MarkerOptions
    private var endMarker: Marker? = null

    private val deferred: Deferred<Unit>

    init {
        val style = PolylineStyle()
        polylineOptions = mutableListOf()
        polylines = mutableListOf()
        startMarkerOptions = MarkerOptions()
        endMarkerOptions = MarkerOptions()

        deferred = GlobalScope.async {
            paths = segment.toPath()
            val firstPoint = paths[0].points[0]
            val lastPath = paths[paths.size - 1]
            val lastPoint = lastPath.points[lastPath.points.size - 1]

            var endOfLastPath: LatLng? = null
            paths.forEach {path ->
                val opts = PolylineOptions()
                if(endOfLastPath != null)
                    opts.add(endOfLastPath)
                opts.addAll(path.points)
                    .color(style.pathColor)
                    .pattern(style.patternPolygonAlpha)
                polylineOptions.add(opts)
                endOfLastPath = path.points[path.points.size - 1]
            }

            startMarkerOptions.position(firstPoint)
                .title(Helper.capitalizeWords(startName))
                .snippet("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

            endMarkerOptions.position(lastPoint)
                .title(Helper.capitalizeWords(endName))
                .snippet("Destination")
            Unit
        }
    }

    fun addToMap(map: Map, floor: Int) {
        for(i in paths.indices) {
            val path = paths[i]
            val opts = polylineOptions[i]
            if(path.shouldDisplay(floor)) {
                opts.zIndex(5F)
            } else {
                opts.zIndex(1F)
            }
        }
        polylineOptions.forEach { line ->
            polylines.add(map.addPolyline(line)!!)
        }
        startMarker = map.addMarker(startMarkerOptions)
        endMarker = map.addMarker(endMarkerOptions)
    }

    fun removeFromMap() {
        polylines.forEach {
            it.remove()
        }
        polylines = mutableListOf()
        startMarker?.remove()
        endMarker?.remove()
    }

    suspend fun waitUntilCreated() {
        deferred.await()
    }

    fun getPathBounds(): LatLngBounds {
        var north: Double = paths[0].points[0].latitude
        var south: Double = paths[0].points[0].latitude
        var east: Double = paths[0].points[0].longitude
        var west: Double = paths[0].points[0].longitude

        paths.forEach {path ->
            path.points.forEach {point ->
                north = Math.max(north, point.latitude)
                south = Math.min(south, point.latitude)
                east = Math.max(east, point.longitude)
                west = Math.min(west, point.longitude)
            }
        }

        val southwest = LatLng(south, west)
        val northeast = LatLng(north, east)

        return LatLngBounds(southwest, northeast)
    }
}
