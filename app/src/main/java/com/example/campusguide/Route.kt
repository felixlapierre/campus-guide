package com.example.campusguide

import android.app.Activity
import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.internal.PolylineEncoding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Route constructor(private val map: GoogleMap, ctx: Activity, start: String, end: String) {
    private lateinit var polyline: Polyline
    init {
        val directions = Directions(ctx)
        GlobalScope.launch {
            val response = directions.getDirections(start, end)
            if(response != null) {
                val line = response.routes[0].overviewPolyline.points
                val decoded = PolylineEncoding.decode(line)
                val decodedAsGoodLatLng = decoded.map {
                    LatLng(it.lat, it.lng)
                }
                ctx.runOnUiThread {
                    map.addPolyline(PolylineOptions().addAll(decodedAsGoodLatLng))
                }
            }
        }
    }
}