package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.InfoWindowData

interface Marker {
    fun remove()
    fun showInfoWindow()
    fun setTag(info: InfoWindowData)
}
