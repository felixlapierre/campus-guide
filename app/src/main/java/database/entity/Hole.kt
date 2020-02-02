package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Hole(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="highlight_id") val highlightId: Int
)