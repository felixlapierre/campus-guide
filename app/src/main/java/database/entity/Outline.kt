package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Outline (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="highlight_id") val highlightId: Int
)
