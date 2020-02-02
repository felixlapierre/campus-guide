package database

import android.content.Context
import androidx.room.Room

class RoomDatabaseSingleton private constructor() {
    companion object{
        private lateinit var instance: AppDatabase


        fun getInstance(applicationContext: Context): AppDatabase{
            if(::instance.isInitialized){
                instance = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").build()
            }
            return instance
        }
    }
}