package com.example.campusguide.directions

import com.example.campusguide.search.indoor.Building
import com.example.campusguide.search.indoor.BuildingIndexSingleton
import com.google.android.gms.maps.model.LatLng

class LocationMetadata constructor(
    public val name: String,
    public val encoded: String,
    private val buildingIndexSingleton: BuildingIndexSingleton? = null
) {
    public fun getLatLng(): LatLng {
        // If encoded value is Lat, Lng
        return if (encoded.matches("""\d+.\d+,\s*-?\d+.\d+""".toRegex())) {
            val latlngStrList = encoded.split(""",\s*""".toRegex())
            LatLng(
                latlngStrList[0].toDouble(),
                latlngStrList[1].toDouble()
            )
        }
        // If encoded value is room code
        else {
            val building: Building = buildingIndexSingleton!!.findBuildingByCode("LB")!!
            val lat = building.lat.toDouble()
            val lng = building.lon.toDouble()
            LatLng(lat, lng)
        }
    }
}
