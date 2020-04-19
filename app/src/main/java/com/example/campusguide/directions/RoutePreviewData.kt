package com.example.campusguide.directions

import java.io.Serializable

open class RoutePreviewData : Serializable {
    private var steps: List<GoogleDirectionsAPIStep> = emptyList()
    private var path: List<Path> = emptyList()
    private var start: String = ""
    private var end: String = ""
    private var distance: String = ""
    private var duration: Int = 0

    fun setDuration(d: Int) {
        duration = d
    }

    fun getDuration(): Int {
        return duration
    }

    fun setDistance(s: String) {
        distance = s
    }

    fun getDistance(): String {
        return distance
    }

    fun setStart(s: String) {
        start = s
    }

    fun getStart(): String {
        return start
    }

    fun setEnd(s: String) {
        end = s
    }

    fun getEnd(): String {
        return end
    }

    fun setSteps(set: List<GoogleDirectionsAPIStep>) {
        steps = set
    }

    fun getSteps(): List<GoogleDirectionsAPIStep> {
        return steps
    }

    fun setPath(pathPolyline: List<Path>) {
        path = pathPolyline
    }

    fun getPath(): List<Path> {
        return path
    }
}
