package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Highlight (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="outline") val outlines: List<Outline>,
    @ColumnInfo(name="hole") val holes: List<Hole>
)