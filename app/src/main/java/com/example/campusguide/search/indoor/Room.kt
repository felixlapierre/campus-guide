package com.example.campusguide.search.indoor

import com.beust.klaxon.Json

data class Room (
    @Json(name = "name")
    val name: String,

    @Json(name = "code")
    val code: String,

    @Json(name = "lat")
    val lat: String,

    @Json(name = "lon")
    val lon: String
)