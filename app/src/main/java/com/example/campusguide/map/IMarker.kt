package com.example.campusguide.map

import com.example.campusguide.map.infoWindow.MarkerTag

interface IMarker {
    fun remove()
    fun showInfoWindow()
    fun setTag(info: MarkerTag)
}
