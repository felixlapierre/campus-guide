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

            val Hbuilding = Building("Henry F. Hall Building", "H")
            val highlight = Highlight()
            val outline = Outline()
            outline.points.add(Point(0,45.497165, -73.579545))
            outline.points.add(Point(1, 45.497710, -73.579034))
            outline.points.add(Point(2, 45.497373, -73.578338))
            outline.points.add(Point(3, 45.496830, -73.578850))
            highlight.outlines.add(outline)
            Hbuilding.highlight.target = highlight
            buildingBox.put(Hbuilding)


            //testing
            val buildings = buildingBox.all
            Log.d("BUILDINGS", buildings.toString())
        }
    }
}

