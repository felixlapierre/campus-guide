package com.example.campusguide

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import database.ObjectBox
import database.entity.Building
import database.entity.Highlight
import database.entity.Point
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class BuildingHighlights(private val googleMap: GoogleMap) {
    fun addBuildingHighlights(){
        val buildingBox: Box<Building> =  ObjectBox.boxStore.boxFor()
        for(building in buildingBox.all){
            for(outline in building.highlight.target.outlines){
                val polygon = PolygonOptions()
                for(point in outline.points.sortedBy { it.order }){
                    polygon.add(LatLng(point.latitude, point.longitude))
                }
                googleMap.addPolygon(polygon)
            }
            for(hole in building.highlight.target.holes){
                val polygon = PolygonOptions()
                for(point in hole.points.sortedBy { it.order }){
                    polygon.add(LatLng(point.latitude, point.longitude))
                }
                googleMap.addPolygon(polygon)
            }
        }
    }
}