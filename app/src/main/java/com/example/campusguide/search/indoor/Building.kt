package com.example.campusguide.search.indoor

import com.beust.klaxon.Json

data class Building(
    @Json(name = "name")
    val name: String,

    @Json(name = "code")
    val code: String,

    @Json(name = "address")
    val address: String,

    @Json(name = "services")
    val services: String,

    @Json(name = "lat")
    val lat: String,

    @Json(name = "lon")
    val lon: String,

    @Json(name = "rooms")
    val rooms: List<Room>,

    @Json(name = "nodes")
    val nodes: List<Node>
)
