package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highlight")
data class Highlight (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="building_id") val buildingId: Int
)