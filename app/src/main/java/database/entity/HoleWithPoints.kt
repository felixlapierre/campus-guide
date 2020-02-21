//package database.entity
//
//import androidx.room.Embedded
//import androidx.room.Relation
//
//data class HoleWithPoints (
//    @Embedded val hole: Hole,
//    @Relation(
//        parentColumn = "uid",
//        entityColumn = "polygon_id"
//    )
//    val points: List<Point>
//)