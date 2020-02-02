package database

import Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Hole(
    @PrimaryKey val uid: Int,
    @ColumnInfo val points: List<Point>
)