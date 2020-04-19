package com.example.campusguide.directions.indoor

import com.example.campusguide.Accessibility
import com.example.campusguide.directions.GoogleDirectionsAPIStep
import com.example.campusguide.directions.Path
import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.search.indoor.Building

class IndoorSegment constructor(
    private val buildingCode: String,
    private val startRoomCode: String,
    private val args: SegmentArgs
) :
    Segment {
    private val pathfinding: FindRoomPathfinding
    private var endRoomCode: String? = null
    private var next: Segment? = null
    private val building: Building = args.buildingIndex.findBuildingByCode(buildingCode)
        ?: throw RuntimeException("Cannot create IndoorSegment: building $buildingCode not found.")

    constructor(startEncoded: String, args: SegmentArgs) : this(
        startEncoded.split("_")[1],
        startEncoded.split("_")[2],
        args
    )
    init {
        pathfinding = AccessibleRoomPathfinding(Graph(building), Accessibility.forbiddenRooms)
    }

    override fun setNext(next: IndoorSegment) {
        if (next.buildingCode == buildingCode) {
            this.next = next
            endRoomCode = next.startRoomCode
        } else {
            endRoomCode = building.nodes[0].code
            val segmentFromMyBuildingToTheirs =
                OutdoorSegment(building.address, args)
            segmentFromMyBuildingToTheirs.setNext(next)
            this.next = segmentFromMyBuildingToTheirs
        }
    }

    override fun setNext(next: OutdoorSegment) {
        endRoomCode = building.nodes[0].code
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

    override suspend fun toPath(): List<Path> {
        return if (endRoomCode != null) {
            val result = mutableListOf<Path>()
            result.addAll(pathfinding.findRoom(startRoomCode, endRoomCode!!)[0])
            result.addAll(next?.toPath() ?: emptyList())
            return result
        } else
            emptyList()
    }

    override fun getDuration(): Int {
        // TODO: Estimate duration of indoor path segments
        return next?.getDuration() ?: 0
    }

    override fun getDistance(): String {
        // TODO: Estimate distance of indoor path segments
        return ""
    }

    override fun getSteps(): List<GoogleDirectionsAPIStep> {
        return emptyList()
    }

    override fun getFare(): String {
        return ""
    }
}
