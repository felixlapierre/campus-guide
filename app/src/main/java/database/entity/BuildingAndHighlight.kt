package database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BuildingAndHighlight (
    @Embedded val building: Building,
    @Relation(
        parentColumn = "uid",
        entityColumn = "building_id"
    )
    val highlight: Highlight
)