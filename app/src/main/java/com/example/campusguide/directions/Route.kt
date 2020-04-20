package com.example.campusguide.directions

import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class Route constructor (
    private val start: LocationMetadata,
    private val end: LocationMetadata,
    private val travelMode: String,
    private val transitPreference: String?,
    private val buildingIndexSingleton: BuildingIndexSingleton,
    private val giveMeAnOutdoorDirections: () -> OutdoorDirections
) {
    private var pathPolylines: MutableList<PathPolyline>? = mutableListOf()

    init {
        when (travelMode) {
            "shuttle" -> {
                if (shouldBeShuttleRoute(start, end)) {
                    pathPolylines = mutableListOf()
                    createShuttlePathPolylineList(start, end)
                }
            }
            else -> {
                pathPolylines = mutableListOf()
                pathPolylines!!.add(createPathPolyline(
                    start, end, travelMode, transitPreference
                ))
            }
        }
    }

    public fun removeFromMap() {
        pathPolylines!!.forEach {
            it.removeFromMap()
        }
    }
    public fun getPathPolylines(): List<PathPolyline>? {
        return this.pathPolylines
    }

    public fun getDuration(): Int {
        var duration = 0
        this.pathPolylines!!.forEach {
            duration += it.getDuration()
        }
        return duration
    }

    public suspend fun waitUntilCreated() {
        this.pathPolylines!!.forEach {
            it.waitUntilCreated()
        }
    }

    private fun isIndoorLocation(encodedLocation: String): Boolean {
        return encodedLocation.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)
    }

    private fun createShuttlePathPolylineList(start: LocationMetadata, end: LocationMetadata) {
        when (campusFromLatLng(start.getLatLng())) {
            Campus.DOWNTOWN -> {
                pathPolylines!!.add(
                    createPathPolyline(start, ShuttleBusStopSGW(), "walking", null, ShuttlePath.TO_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopSGW(), ShuttleBusStopLOYOLA(), "driving", null, ShuttlePath.ON_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopLOYOLA(), end, "walking", null, ShuttlePath.FROM_SHUTTLE))
            }
            Campus.LOYOLA -> {
                pathPolylines!!.add(
                    createPathPolyline(start, ShuttleBusStopLOYOLA(), "walking", null, ShuttlePath.TO_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopLOYOLA(), ShuttleBusStopSGW(), "driving", null, ShuttlePath.ON_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopSGW(), end, "walking", null, ShuttlePath.FROM_SHUTTLE))
            }
        }
    }

    private enum class ShuttlePath {
        TO_SHUTTLE,
        FROM_SHUTTLE,
        ON_SHUTTLE,
        NOT_SHUTTLE
    }

    private fun createPathPolyline(
        start: LocationMetadata,
        end: LocationMetadata,
        travelMode: String,
        transitPreference: String?,
        shuttlePath: ShuttlePath = ShuttlePath.NOT_SHUTTLE
    ): PathPolyline {

        val directions = giveMeAnOutdoorDirections()
        val segmentArgs =
            SegmentArgs(travelMode, buildingIndexSingleton, directions, transitPreference)

        val firstSegment = createSegment(start.encoded, segmentArgs)
        val secondSegment = createSegment(end.encoded, segmentArgs)
        secondSegment.appendTo(firstSegment)

        when (shuttlePath) {
            ShuttlePath.TO_SHUTTLE -> {
                return PathPolyline(start.name, end.name, firstSegment,
                    endMarkerOptions = MarkerOptions().alpha(0.0f))
            }
            ShuttlePath.ON_SHUTTLE -> {
                return PathPolyline(start.name, end.name, firstSegment,
                    style = PolylineStyle(patternGap = Gap(0f)),
                    startMarkerOptions = MarkerOptions().icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_shuttle_departure))
                        .title("Shuttle Departure Location")
                        // Will be true until the project is due
                        // No further maintenance will be required :)
                        .snippet("Note: No shuttle due to COVID-19"),
                    endMarkerOptions = MarkerOptions().icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_shuttle_departure))
                        .title("Shuttle Drop-off Location")
                        // Will be true until the project is due
                        // No further maintenance will be required :)
                        .snippet("Note: No shuttle due to COVID-19")
                )
            }
            ShuttlePath.FROM_SHUTTLE -> {
                return PathPolyline(start.name, end.name, firstSegment,
                    startMarkerOptions = MarkerOptions().alpha(0.0f))
            }
            else -> {
                if (travelMode == Constants.TRAVEL_MODE_DRIVING)
                    return PathPolyline(start.name, end.name, firstSegment,
                        style = PolylineStyle(patternGap = Gap(0f)))
                return PathPolyline(start.name, end.name, firstSegment)
            }
        }
    }

    private fun createSegment(location: String, args: SegmentArgs): Segment {
        return if (isIndoorLocation(location))
            IndoorSegment(location, args)
        else OutdoorSegment(location, args)
    }

    fun getDistance(): String {
        var totalDistance = ""
        if (pathPolylines.isNullOrEmpty())
            return ""
        if (pathPolylines!!.size > 1) {
            pathPolylines!!.forEach {
                totalDistance += "${it.getDistance()} + "
            }
            totalDistance.subSequence(0, totalDistance.length - 3)
        } else {
            totalDistance = pathPolylines!!.first().getDistance()
        }
        return totalDistance
    }

    suspend fun getRoutePreviewData(): RoutePreviewData {
        waitUntilCreated()
        if (pathPolylines!!.size == 1)
            return pathPolylines!![0].getRoutePreviewData()
        val routePreviewData = RoutePreviewData()
        routePreviewData.setDistance(getDistance())
        routePreviewData.setDuration(getDuration())
        routePreviewData.setStart(start.name)
        routePreviewData.setEnd(end.name)
        routePreviewData.setSteps(getSteps())
        routePreviewData.setPath(getPathsRisky())
        return routePreviewData
    }
    fun getRoutePreviewDataRisky(): RoutePreviewData {
        if (pathPolylines!!.size == 1)
            return pathPolylines!![0].getRoutePreviewData()
        val routePreviewData = RoutePreviewData()
        routePreviewData.setDistance(getDistance())
        routePreviewData.setDuration(getDuration())
        routePreviewData.setStart(start.name)
        routePreviewData.setEnd(end.name)
        routePreviewData.setSteps(getSteps())
        routePreviewData.setPath(getPathsRisky())
        return routePreviewData
    }

    private fun getPathsRisky(): List<Path> {
        val paths: MutableList<Path> = mutableListOf()
        pathPolylines!!.forEach {
            paths += it.getPathsRisky()
        }
        return paths
    }

    fun getSteps(): List<GoogleDirectionsAPIStep> {
        val steps: MutableList<GoogleDirectionsAPIStep> = mutableListOf()
        steps += pathPolylines!![0].getSteps()
        steps.add(getShuttleBusGoogleAPIStepRisky(pathPolylines!![0]))
        if (pathPolylines!!.size > 1)
            steps += pathPolylines!![2].getSteps()
        return steps
    }

    fun getRouteBounds(): LatLngBounds {
        var latLngBoundsBuilder = LatLngBounds.builder()
        pathPolylines!!.forEach {
            latLngBoundsBuilder.include(it.getPathBounds().southwest)
            latLngBoundsBuilder.include(it.getPathBounds().northeast)
        }
        return latLngBoundsBuilder.build()
    }

    fun getFare(): String {
        if (pathPolylines.isNullOrEmpty())
            return ""
        if (pathPolylines!!.size > 1) {
            return ""
        }
        return pathPolylines!!.first().getFare()
    }
}
