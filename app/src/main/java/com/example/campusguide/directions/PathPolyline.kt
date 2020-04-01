package com.example.campusguide.directions

import com.example.campusguide.utils.Helper
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class PathPolyline constructor(startName: String, endName: String, segment: Segment) {
    class PolylineStyle {
        private val patternDashLengthPx = 20.0f
        private val patternGapLengthPx = 20.0f
        private val patternDash: PatternItem = Dash(patternDashLengthPx)
        private val patternGap: PatternItem = Gap(patternGapLengthPx)
        val patternPolygonAlpha = listOf(patternGap, patternDash)
        val colorBlueArgb = 0xff0000ff
    }

    val polyline: PolylineOptions
    val startMarker: MarkerOptions
    val endMarker: MarkerOptions
    private val deferred: Deferred<Unit>

    init {
        val style = PolylineStyle()
        polyline = PolylineOptions()
        startMarker = MarkerOptions()
        endMarker = MarkerOptions()

        deferred = GlobalScope.async {
            val path = segment.toListOfCoordinates()
            polyline.addAll(path)
                .color(style.colorBlueArgb.toInt())
                .pattern(style.patternPolygonAlpha)

            val firstPoint = path[0]
            startMarker.position(firstPoint)
                .title(Helper.capitalizeWords(startName))
                .snippet("Start")

            val lastPoint = path[path.size - 1]
            endMarker.position(lastPoint)
                .title(Helper.capitalizeWords(endName))
                .snippet("Destination")
            Unit
        }
    }

    suspend fun waitUntilCreated() {
        deferred.await()
    }
}