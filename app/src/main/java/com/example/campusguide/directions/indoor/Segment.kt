package com.example.campusguide.directions.indoor

import com.example.campusguide.map.Map

/**
 * Represents a segment of a route.
 */
interface Segment {
    fun setNext(next: IndoorSegment)
    fun setNext(next: OutdoorSegment)
    fun display(map: Map)
}