package com.example.campusguide.directions

import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.map.Map

/**
 * Represents a segment of a route.
 */
interface Segment {
    fun setNext(next: IndoorSegment)
    fun setNext(next: OutdoorSegment)
    fun appendTo(segment: Segment)
    fun display(map: Map)
}