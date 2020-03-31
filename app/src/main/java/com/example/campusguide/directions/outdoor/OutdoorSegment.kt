package com.example.campusguide.directions.outdoor

import com.example.campusguide.directions.indoor.IndoorSegment
import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.map.Map
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OutdoorSegment(private val start: String, private val args: SegmentArgs) : Segment {
    private val route = OutdoorRoute(args.outdoorDirections)
    private var next: Segment? = null
    private var routingTask: Deferred<Unit>? = null

    override fun setNext(next: IndoorSegment) {
        routingTask?.cancel()
        routingTask = GlobalScope.async {
            route.set(start, next.getBuilding().address, args.travelMode)
        }
        val nextSegment = IndoorSegment(next.getBuilding().nodes[0].code, args)
        nextSegment.setNext(next)
        this.next = nextSegment
    }

    override fun setNext(next: OutdoorSegment) {
        routingTask?.cancel()
        routingTask = GlobalScope.async {
            route.set(start, next.start, args.travelMode)
        }
        this.next = next
    }

    override fun display(map: Map) {
        GlobalScope.launch {
            if(routingTask?.await() != null) {
                route.display(map)
            }
        }
        next?.display(map)
    }
}