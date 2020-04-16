package com.example.campusguide.directions

data class TransitRoute constructor(
    val title: String,
    val steps: List<GoogleDirectionsAPIStep>,
    val duration: Int
)