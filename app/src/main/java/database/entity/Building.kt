package database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Building(
    @Id var id: Long = 0,
    var fullName: String? = null,
    var abbreviationName: String? = null
)