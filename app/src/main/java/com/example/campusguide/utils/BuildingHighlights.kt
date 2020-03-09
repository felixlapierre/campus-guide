package com.example.campusguide.utils

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import database.ObjectBox
import database.entity.Building
import database.entity.Point
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class BuildingHighlights(private val googleMap: GoogleMap) {

    fun addBuildingHighlights(){
        val buildingBox: Box<Building> =  ObjectBox.boxStore.boxFor()
        for(building in buildingBox.all){
            for(outline in building.highlight.target.outlines){
                val polygon = PolygonOptions()
                polygon.addAll(getLatLngList(outline.points))
                for(hole in outline.holes){
                    polygon.addHole(getLatLngList(hole.points))
                }
                polygon.clickable(true)
                googleMap.addPolygon(polygon)
            }
        }
    }

    private fun getLatLngList(points: Collection<Point>): Collection<LatLng>{
        val latLngs = mutableListOf<LatLng>()
        for(point in points.sortedBy { it.order }){
            latLngs.add(LatLng(point.latitude, point.longitude))
        }
        return latLngs
    }
}