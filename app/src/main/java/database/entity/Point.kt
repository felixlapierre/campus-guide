package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point")
data class Point(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="polygon_id") val polygonId: Int,
    @ColumnInfo(name="latitude") val latitude: Double,
    @ColumnInfo(name="longitude") val longitude: Double,
    @ColumnInfo(name="order") val order: Int
)