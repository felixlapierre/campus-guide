package com.example.campusguide.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import com.example.campusguide.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import database.ObjectBox
import database.entity.Building
import database.entity.Point
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class BuildingHighlights(private val googleMap: GoogleMap, private val context: Context) {

    fun addBuildingHighlights(){
        val buildingBox: Box<Building> =  ObjectBox.boxStore.boxFor()
        for(building in buildingBox.all){
            for(outline in building.highlight.target.outlines){
                val polygon = PolygonOptions()
                polygon.addAll(getLatLngList(outline.points))
                for(hole in outline.holes){
                    polygon.addHole(getLatLngList(hole.points))
                }
                applyPolygonStyle(polygon)
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

    private fun applyPolygonStyle(polygon: PolygonOptio){
        val primaryColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val darkColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        polygon.strokeWidth(3.5F)
        polygon.strokeColor(darkColor)
        polygon.fillColor(ColorUtils.setAlphaComponent(primaryColor, 40))
    }
}