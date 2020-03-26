package database

import android.content.Context
import database.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    val DB_DIRECTORY_NAME = "database"
    val DB_FILE_NAME  = "data.mdb"

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .initialDbFile(DatabaseStreamFactory(context.assets.open(DB_DIRECTORY_NAME+"/"+DB_FILE_NAME)))
            .androidContext(context.applicationContext)
            .build()
    }
}