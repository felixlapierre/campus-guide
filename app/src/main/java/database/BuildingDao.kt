package database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BuildingDao{
    @Query("SELECT * FROM building")
    fun getAll(): List<Building>
}