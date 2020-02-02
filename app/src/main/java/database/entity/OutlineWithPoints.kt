package database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class OutlineWithPoints (
    @Embedded val outline: Outline,
    @Relation(
        parentColumn = "uio",
        entityColumn = "polygon_id"
    )
    val points: List<Point>
)