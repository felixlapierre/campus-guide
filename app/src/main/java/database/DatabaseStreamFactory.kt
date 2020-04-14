package database

import io.objectbox.Factory
import java.io.InputStream

class DatabaseStreamFactory(private val databaseInputStream: InputStream) : Factory<InputStream> {
    override fun provide(): InputStream {
        return databaseInputStream
    }
}
