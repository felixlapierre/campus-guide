package com.example.campusguide.directions

import com.example.campusguide.directions.outdoor.OutdoorDirections
import com.example.campusguide.search.indoor.BuildingIndex

class SegmentArgs(
    var travelMode: String,
    var buildingIndex: BuildingIndex,
    var outdoorDirections: OutdoorDirections,
    var transitPreference: String?
)
