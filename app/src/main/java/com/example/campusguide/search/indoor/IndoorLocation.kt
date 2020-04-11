package com.example.campusguide.search.indoor

import com.example.campusguide.search.SearchLocation

class IndoorLocation(name: String, lat: Double, lon: Double, id: String, secondaryText: String) :
    SearchLocation(name, lat, lon, id, secondaryText) {
    override fun encodeForDirections(): String {
        return id
    }
}