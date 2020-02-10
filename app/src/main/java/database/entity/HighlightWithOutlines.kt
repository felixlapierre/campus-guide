//package database.entity
//
//import androidx.room.Embedded
//import androidx.room.Relation
//
//data class HighlightWithOutlines (
//    @Embedded val highlight: Highlight,
//    @Relation(
//        parentColumn = "uid",
//        entityColumn = "highlight_id"
//    )
//    val outlines : List<Outline>
//)