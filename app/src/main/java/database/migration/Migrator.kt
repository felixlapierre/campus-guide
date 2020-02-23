package database.migration

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import database.ObjectBox
import database.migration.Migration0_1


class Migrator: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectBox.init(this.applicationContext)
        Migration0_1.migrate(this.applicationContext)
        this.finishAndRemoveTask()
    }
}