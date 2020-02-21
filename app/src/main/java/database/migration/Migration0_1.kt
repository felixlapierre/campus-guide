package database.migration

import android.util.Log
import database.ObjectBox
import database.entity.*
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class Migration0_1{
    companion object{
        fun migrate(){
            val buildingBox: Box<Building> =  ObjectBox.boxStore.boxFor()
            buildingBox.removeAll()

            val building = Building("Henry F. Hall Building", "H")
            val highlight = Highlight()
            val outline = Outline()
            outline.points.add(Point(0, 0.0, 0.0))
            highlight.outlines!!.add(outline)
            building.highlight.target = highlight
            buildingBox.put(building)

            //testing
            val buildings = buildingBox.all
            Log.d("BUILDINGS", buildings.toString())
        }
    }
}

