package com.example.campusguide.directions.indoor

import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.search.indoor.Building
import com.google.android.gms.maps.model.LatLng

class IndoorSegment constructor(
    private val buildingCode: String,
    private val startRoomCode: String,
    private val args: SegmentArgs
) :
    Segment {
    private val route = IndoorRoute(args.buildingIndex)
    private var next: Segment? = null
    private val building: Building = args.buildingIndex.findBuildingByCode(buildingCode)
        ?: throw RuntimeException("Cannot create IndoorSegment: building $buildingCode not found.")

    constructor(startEncoded: String, args: SegmentArgs) : this(
        startEncoded.split("_")[1],
        startEncoded.split("_")[2],
        args
    )

    override fun setNext(next: IndoorSegment) {
        if (next.buildingCode == buildingCode) {
            this.next = next
            route.set(buildingCode, startRoomCode, next.startRoomCode)
        } else {
            route.set(buildingCode, startRoomCode, building.nodes[0].code)
            val segmentFromMyBuildingToTheirs =
                OutdoorSegment(building.address, args)
            segmentFromMyBuildingToTheirs.setNext(next)
            this.next = segmentFromMyBuildingToTheirs
        }
    }

    override fun setNext(next: OutdoorSegment) {
        route.set(buildingCode, startRoomCode, building.nodes[0].code)
        val segmentFromMyBuildingToOutdoorSegment =
            OutdoorSegment(building.address, args)
        segmentFromMyBuildingToOutdoorSegment.setNext(next)
        this.next = segmentFromMyBuildingToOutdoorSegment
    }

    override fun appendTo(segment: Segment) {
        segment.setNext(this)
    }

    fun getBuilding(): Building {
        return building
    }

    override suspend fun toListOfCoordinates(): List<LatLng> {
        val result = mutableListOf<LatLng>()
        result.addAll(route.getLine())
        result.addAll(next?.toListOfCoordinates() ?: listOf())
        return result
    }
}