package database.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class Outline(
    @Id var id: Long = 0
){
    lateinit var highlight: ToOne<Highlight>
    @Backlink(to = "outline")
    lateinit var points: ToMany<Point>
    @Backlink(to = "outline")
    lateinit var holes: ToMany<Hole>
}