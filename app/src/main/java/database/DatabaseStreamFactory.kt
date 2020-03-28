package database

import android.renderscript.ScriptGroup
import io.objectbox.Factory
import io.objectbox.android.ObjectBoxDataSource
import java.io.InputStream

class DatabaseStreamFactory(private val databaseInputStream: InputStream) : Factory<InputStream> {
    override fun provide(): InputStream {
        return databaseInputStream
    }

}