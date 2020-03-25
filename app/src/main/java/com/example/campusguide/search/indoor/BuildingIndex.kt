package com.example.campusguide.search.indoor

/**
 * Represents a class that can provide the buildings that are indexed
 * for indoor room search
 */
interface BuildingIndex {
    fun getBuildings(): List<Building>?
}