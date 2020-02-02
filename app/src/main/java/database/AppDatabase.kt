package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import database.dao.BuildingDao
import database.entity.*

@Database(entities = arrayOf(Point::class, Hole::class, Outline::class, Highlight::class, Building::class), version = 1)
abstract class AppDatabase private constructor() : RoomDatabase() {
    abstract fun BuildingDao(): BuildingDao
    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "Sample.db")
                .build()
    }
}