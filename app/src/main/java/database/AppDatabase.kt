package database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BuildingDao::class), version = 1)
abstract class AppDatabase private constructor() : RoomDatabase() {
    abstract fun BuildingDao(): BuildingDao
}