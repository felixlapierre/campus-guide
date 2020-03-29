package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLng

class RouteStartEnd constructor(
    val start: LatLng,
    val end: LatLng
) {
    val startString: String = "${start.latitude},${start.longitude}"
    val endString: String = "${end.latitude},${end.longitude}"
}