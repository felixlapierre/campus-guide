package com.example.campusguide.directions.outdoor

import com.example.campusguide.directions.GoogleDirectionsAPIStep
import com.example.campusguide.directions.Path
import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.indoor.IndoorSegment
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
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
            route.set(start, next.getBuilding().address, args.travelMode, args.transitPreference)
        }
        val nextSegment = IndoorSegment(next.getBuilding().code, next.getBuilding().nodes[0].code, args)
        nextSegment.setNext(next)
        this.next = nextSegment
    }

    override fun setNext(next: OutdoorSegment) {
        routingTask?.cancel()
        routingTask = GlobalScope.async {
            route.set(start, next.start, args.travelMode, args.transitPreference)
        }
        this.next = next
    }

    override fun appendTo(segment: Segment) {
        segment.setNext(this)
    }

    override suspend fun toPath() = suspendCoroutine<List<Path>> { cont ->
        if (next == null) {
            cont.resume(emptyList())
        }
        GlobalScope.launch {
            if (routingTask?.await() != null) {
                val result = mutableListOf<Path>()
                result.add(Path(route.getLine().toMutableList()))
                result.addAll(next?.toPath() ?: emptyList())
                cont.resume(result)
            }
        }
    }

    override fun getDuration(): Int {
        return route.getDuration() + (next?.getDuration() ?: 0)
    }

    override fun getSteps(): List<GoogleDirectionsAPIStep> {
        return route.getSteps()
    }

    override fun getFare(): String {
        return route.getFare()
    }

    override fun getDistance(): String {
        return route.distance
    }
}
