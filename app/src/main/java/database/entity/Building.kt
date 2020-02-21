package database.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Building(
    @Id var id: Long = 0,
    var fullName: String,
    var abbreviationName: String
){
    lateinit var highlight: ToOne<Highlight>
    constructor(fullName: String, abbreviationName: String): this (0, fullName, abbreviationName)
}