package com.example.campusguide.directions.outdoor

import com.example.campusguide.directions.Segment
import com.example.campusguide.directions.SegmentArgs
import com.example.campusguide.directions.indoor.IndoorSegment
import com.google.android.gms.maps.model.LatLng
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

    override suspend fun toListOfCoordinates() = suspendCoroutine<List<LatLng>> { cont ->
        if (next == null) {
            cont.resume(emptyList())
        }
        GlobalScope.launch {
            if (routingTask?.await() != null) {
                val result = mutableListOf<LatLng>()
                result.addAll(route.getLine())
                result.addAll(next?.toListOfCoordinates() ?: emptyList())
                cont.resume(result)
            }
        }
    }

    override fun getDuration(): Int {
        return route.duration
    }
}
