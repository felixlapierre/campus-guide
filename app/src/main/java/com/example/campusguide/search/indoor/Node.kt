package com.example.campusguide.search.indoor

import com.beust.klaxon.Json

data class Node(
    @Json(name = "name")
    val name: String,

    @Json(name = "code")
    val code: String,

    @Json(name = "edges")
    val edges: List<String>
)