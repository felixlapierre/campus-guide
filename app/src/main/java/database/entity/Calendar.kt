package database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Calendar(
    @Id(assignable = true)
    var id: Long,
    var name: String = ""
)
