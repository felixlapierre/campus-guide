package com.example.campusguide.directions

import com.example.campusguide.Constants
import com.example.campusguide.R
import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.MarkerOptions

class Route constructor (
    private val start: LocationMetadata,
    private val end: LocationMetadata,
    private val travelMode: String,
    private val buildingIndexSingleton: BuildingIndexSingleton,
    private val giveMeAnOutdoorDirections: () -> OutdoorDirections
) {
    private var pathPolylines: MutableList<PathPolyline>? = null

    init {
        when(travelMode) {
            "shuttle" -> {
                if (shouldBeShuttleRoute(start, end)) {
                    pathPolylines = mutableListOf<PathPolyline>()
                    createShuttlePathPolylineList(start, end)
                }
            }
            else -> {
                pathPolylines = mutableListOf<PathPolyline>()
                pathPolylines!!.add(createPathPolyline(
                    start, end, travelMode
                ))
            }
        }
    }
    public fun removeFromMap() {
        pathPolylines?.forEach{
            it.removeFromMap()
        }
    }
    public fun getPathPolylines(): List<PathPolyline>? {
        return this.pathPolylines
    }

    public fun getDuration(): Int {
        var duration = 0
        this.pathPolylines?.forEach {
            duration += it.segment.getDuration()
        }
        return duration
    }

    public suspend fun waitUntilCreated() {
        this.pathPolylines?.forEach {
            it.waitUntilCreated()
        }
    }

    private fun isIndoorLocation(encodedLocation: String): Boolean {
        return encodedLocation.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)
    }

    private fun createShuttlePathPolylineList(start: LocationMetadata, end: LocationMetadata) {
        when (campusFromLatLng(start.getLatLng())){
            Campus.DOWNTOWN -> {
                pathPolylines!!.add(
                    createPathPolyline(start, ShuttleBusStopSGW(), "walking", ShuttlePath.TO_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopSGW(), ShuttleBusStopLOYOLA(), "driving", ShuttlePath.ON_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopLOYOLA(), end, "walking", ShuttlePath.FROM_SHUTTLE))
            }
            Campus.LOYOLA -> {
                pathPolylines!!.add(
                    createPathPolyline(start, ShuttleBusStopLOYOLA(), "walking", ShuttlePath.TO_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopLOYOLA(), ShuttleBusStopSGW(), "driving", ShuttlePath.ON_SHUTTLE))
                pathPolylines!!.add(
                    createPathPolyline(ShuttleBusStopSGW(), end, "walking", ShuttlePath.FROM_SHUTTLE))
            }
        }
    }

    private enum class ShuttlePath {
        TO_SHUTTLE,
        FROM_SHUTTLE,
        ON_SHUTTLE,
        NOT_SHUTTLE
    }

    private fun createPathPolyline(start: LocationMetadata, end: LocationMetadata, travelMode: String,
        shuttlePath: ShuttlePath = ShuttlePath.NOT_SHUTTLE
    ): PathPolyline {

        val directions = giveMeAnOutdoorDirections()
        val segmentArgs =
            SegmentArgs(travelMode, buildingIndexSingleton, directions)

        val firstSegment = createSegment(start.encoded, segmentArgs)
        val secondSegment = createSegment(end.encoded, segmentArgs)
        secondSegment.appendTo(firstSegment)

        when(shuttlePath) {
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
                        .snippet("No shuttle due to COVID-19"),
                    endMarkerOptions = MarkerOptions().alpha(0f)
                )
            }
            ShuttlePath.FROM_SHUTTLE -> {
                return PathPolyline(start.name, end.name, firstSegment,
                    startMarkerOptions = MarkerOptions().alpha(0.0f))
            }
            else -> {
                return PathPolyline(start.name, end.name, firstSegment)
            }
        }
    }

    private fun createSegment(location: String, args: SegmentArgs): Segment {
        return if (isIndoorLocation(location))
            IndoorSegment(location, args)
        else OutdoorSegment(location, args)
    }



}
