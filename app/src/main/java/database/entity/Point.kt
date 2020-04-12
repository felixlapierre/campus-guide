package database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Point(
    @Id var id: Long = 0,
    var order: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {
    var outline: ToOne<Outline>? = null
    var hole: ToOne<Hole>? = null
    constructor(order: Int, latitude: Double, longitude: Double) : this(0, order, latitude, longitude)
}
