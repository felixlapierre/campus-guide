package com.example.campusguide.search.indoor

import com.beust.klaxon.Json

data class Node(
    @Json(name = "type")
    val type: String,

    @Json(name = "code")
    val code: String,

    @Json(name = "x")
    val x: Double,

    @Json(name = "y")
    val y: Double,

    @Json(name = "edges")
    val edges: MutableList<String>,

    var floor: Int = 0
)