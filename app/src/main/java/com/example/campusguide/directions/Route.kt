package com.example.campusguide.directions;

import android.location.Location
import com.example.campusguide.Constants
import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.search.indoor.BuildingIndexSingleton

public class Route constructor (
    private val start: LocationMetadata,
    private val end: LocationMetadata,
    private val travelMode: String,
    private val buildingIndexSingleton: BuildingIndexSingleton,
    private val giveMeAnOutdoorDirections: () -> OutdoorDirections
) {
    private lateinit var pathPolylines: MutableList<PathPolyline>

    init {
        when(travelMode) {
            "shuttle" -> {

            }
            else -> {
                pathPolylines.add(createPathPolyline(
                    start, end, travelMode
                ))
            }
        }
    }
    public fun removeFromMap() {
        pathPolylines.forEach{
            it.removeFromMap()
        }
    }
    public fun getPathPolylines(): List<PathPolyline> {
        return this.pathPolylines
    }
    private fun isIndoorLocation(encodedLocation: String): Boolean {
        return encodedLocation.startsWith(Constants.INDOOR_LOCATION_IDENTIFIER)
    }

    suspend fun waitUntilCreated() {
        this.pathPolylines.forEach {
            it.waitUntilCreated()
        }
    }

    fun getDuration(): Int {
        var duration = 0
        this.pathPolylines.forEach {
            duration += it.segment.getDuration()
        }
        return duration
    }

    private fun createPathPolyline(start: LocationMetadata, end: LocationMetadata, travelMode: String): PathPolyline {

        val directions = giveMeAnOutdoorDirections()
        val segmentArgs =
            SegmentArgs(travelMode, buildingIndexSingleton, directions)

        val firstSegment = createSegment(start.encoded, segmentArgs)
        val secondSegment = createSegment(end.encoded, segmentArgs)
        secondSegment.appendTo(firstSegment)

        return PathPolyline(start.name, end.name, firstSegment)
    }

    private fun createSegment(location: String, args: SegmentArgs): Segment {
        return if (isIndoorLocation(location))
            IndoorSegment(location, args)
        else OutdoorSegment(location, args)
    }



}
