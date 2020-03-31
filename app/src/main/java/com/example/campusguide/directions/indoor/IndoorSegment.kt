package com.example.campusguide.directions.indoor

import com.example.campusguide.map.Map
import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndex

class IndoorSegment constructor(start: String, private val index: BuildingIndex) :
    Segment {
    private val route = IndoorRoute(index)
    private var next: Segment? = null
    val buildingCode: String
    val building: Building
    val startRoomCode: String

    init {
        val splitted = start.split("_")
        buildingCode = splitted[1]
        startRoomCode = splitted[2]
        building = index.findBuildingByCode(buildingCode) ?: throw RuntimeException("Cannot create IndoorSegment: building $buildingCode not found.")
    }

    override fun setNext(next: IndoorSegment) {
        if(next.buildingCode == buildingCode) {
            this.next = next
            route.set(buildingCode, startRoomCode, next.startRoomCode)
        } else {
            route.set(buildingCode, startRoomCode, building.rooms[0].code)
            val segmentFromMyBuildingToTheirs = OutdoorSegment()
            segmentFromMyBuildingToTheirs.setNext(next)
            this.next = segmentFromMyBuildingToTheirs
        }
    }

    override fun setNext(next: OutdoorSegment) {
        route.set(buildingCode, startRoomCode, building.rooms[0].code)
        val segmentFromMyBuildingToOutdoorSegment = OutdoorSegment()
        segmentFromMyBuildingToOutdoorSegment.setNext(next)
        this.next = segmentFromMyBuildingToOutdoorSegment
    }

    override fun display(map: Map) {
        route.display(map)
        next?.display(map)
    }
}