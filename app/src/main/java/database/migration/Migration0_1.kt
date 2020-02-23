package database.migration

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import database.ObjectBox
import database.entity.*
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class Migration0_1(){
    companion object{
        fun migrate(applicationContext: Context){
            val buildingHighlightData = JSONArray(applicationContext.assets.open("buildingHighlightData.json").bufferedReader().readText())
            val buildingBox: Box<Building> =  ObjectBox.boxStore.boxFor()
            buildingBox.removeAll()

            for(i in 0 until buildingHighlightData.length()){
                val building = buildingHighlightData.getJSONObject(i)
                val buildingEntity = Building(building.getString("fullName"), building.getString("abbreviationName"))
                val highlight = building.getJSONObject("highlight")
                val highlightEntity = Highlight()

                //Creating the highlight outlines
                val outlines = highlight.getJSONArray("outlines")
                for(j in 0 until outlines.length()){
                    val outlineEntity = Outline()
                    val points = outlines.getJSONArray(j)
                    for(k in 0 until points.length()){
                        outlineEntity.points.addAll(getPointEntityCollection(points))
                    }
                    highlightEntity.outlines.add(outlineEntity)
                }

                //Creating the highlight holes
                val holes = highlight.getJSONArray("holes")
                for(j in 0 until holes.length()){
                    val holeEntity = Hole()
                    val points = outlines.getJSONArray(j)
                    for(k in 0 until points.length()){
                        holeEntity.points.addAll(getPointEntityCollection(points))
                    }
                    highlightEntity.holes.add(holeEntity)
                }
                buildingEntity.highlight.target = highlightEntity
                buildingBox.put(buildingEntity)
            }
        }

        private fun getPointEntityCollection(points: JSONArray): Collection<Point>{
            val pointEntities = mutableListOf<Point>()
            for(i in 0 until points.length()){
                val point = points.getJSONObject(i)
                pointEntities.add(Point(i, point.getDouble("latitude"), point.getDouble("longitude")))
            }
            return pointEntities
        }
    }
}

