package database.dao

import androidx.room.Dao
import androidx.room.Query
import database.entity.Building

@Dao
interface BuildingDao{
    @Query("SELECT * FROM building")
    fun getAll(): List<Building>
}