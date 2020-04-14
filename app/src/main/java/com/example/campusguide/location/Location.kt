package com.example.campusguide.location

open class Location
    constructor(val name: String, val lat: Double, val lon: Double) {

    /**
     * Encode the location in a way that the directions activity will be
     * able to use the location as an origin or destination
     */
    open fun encodeForDirections(): String {
        return "$lat, $lon"
    }
}
