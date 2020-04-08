package database.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class Hole(
    @Id var id: Long = 0
) {
    lateinit var outline: ToOne<Outline>
    @Backlink(to = "hole")
    lateinit var points: ToMany<Point>
}
