package com.example.campusguide.directions

import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.google.android.gms.maps.model.LatLng

/**
 * Represents a segment of a route.
 */
interface Segment {
    fun setNext(next: IndoorSegment)
    fun setNext(next: OutdoorSegment)
    fun appendTo(segment: Segment)
    suspend fun toListOfCoordinates(): List<LatLng>
    fun getDuration(): Int
    fun getSteps(): List<GoogleDirectionsAPIStep>
    fun getFare(): String
}
