package database

import Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Outline (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="point") val points: List<Point>
)
