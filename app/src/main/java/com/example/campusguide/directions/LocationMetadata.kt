package com.example.campusguide.directions

import com.google.android.gms.maps.model.LatLng

class LocationMetadata constructor(
    public val name: String,
    public val encoded: String
) {
    public fun getLatLng():LatLng {
        val latlngStrList = encoded.split(""",\s*""".toRegex())
        return LatLng(
            latlngStrList[1].toDouble(),
            latlngStrList[0].toDouble()
        )
    }

}