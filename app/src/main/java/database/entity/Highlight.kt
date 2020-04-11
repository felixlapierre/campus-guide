package database.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Highlight(
    @Id var id: Long = 0
) {
    @Backlink(to = "highlight")
    lateinit var outlines: ToMany<Outline>
}
