package org.akikon.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Guess : Table() {
    val guess_id: Column<Int> = integer("guess_id").uniqueIndex().autoIncrement()
    val guess: Column<String> = varchar("guess", 30)

    override val primaryKey = PrimaryKey(guess_id, name = "guess_id")
}
