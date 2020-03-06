package com.example.campusguide.location

import android.location.Location

interface LocationProvider {
    fun getLocation(callback: (Location) -> Unit)
}