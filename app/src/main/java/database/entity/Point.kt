package database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Point (
    @Id var id: Long = 0,
    var order: Int,
    var latitude: Double,
    var longitude: Double
){
    lateinit var outline: ToOne<Outline>
    lateinit var hole: ToOne<Hole>
    constructor(order: Int, latitude: Double, longitude: Double): this(0, order, latitude, longitude)
}