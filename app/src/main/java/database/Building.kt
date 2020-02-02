package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Building(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "abbreviation_name") val abbreviationName: String,
    @ColumnInfo(name = "highlight") val highlight: Highlight
)