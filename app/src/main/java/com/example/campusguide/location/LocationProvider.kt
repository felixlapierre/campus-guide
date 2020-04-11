package com.example.campusguide.location

interface LocationProvider {
    fun getLocation(callback: (Location) -> Unit)
}
