package com.example.campusguide.search

import com.example.campusguide.location.Location

open class SearchLocation constructor(
    name: String,
    lat: Double,
    lon: Double,
    val id: String,
    val secondaryText: String
): Location(name, lat, lon) {
    override fun encodeForDirections(): String {
        return "$lat, $lon"
    }
}