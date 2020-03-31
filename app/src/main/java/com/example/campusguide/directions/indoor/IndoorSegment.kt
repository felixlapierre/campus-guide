package com.example.campusguide.directions.indoor

import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.outdoor.OutdoorSegment
import com.example.campusguide.map.Map
import com.example.campusguide.search.indoor.Building

class IndoorSegment constructor(start: String, private val args: SegmentArgs) :
    Segment {
    private val route = IndoorRoute(args.buildingIndex)
    private var next: Segment? = null
    private val buildingCode: String
    private val building: Building
    private val startRoomCode: String

    init {
        val splitted = start.split("_")
        buildingCode = splitted[1]
        startRoomCode = splitted[2]
        building = args.buildingIndex.findBuildingByCode(buildingCode) ?: throw RuntimeException("Cannot create IndoorSegment: building $buildingCode not found.")
    }

    override fun setNext(next: IndoorSegment) {
        if(next.buildingCode == buildingCode) {
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

    override fun display(map: Map) {
        route.display(map)
        next?.display(map)
    }

    fun getBuilding(): Building {
        return building
    }
}