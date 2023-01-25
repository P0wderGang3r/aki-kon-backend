package org.akikon.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Question: Table() {
    val question_id: Column<Int> = integer("question_id").uniqueIndex().autoIncrement()
    val username: Column<String> = varchar("username", 30)
    val question: Column<String> = varchar("question", 100)
    val yes_question_id: Column<Int?> = integer("yes_question_id").nullable()
    val no_question_id: Column<Int?> = integer("no_question_id").nullable()
    val yes_guess_id: Column<Int> = integer("yes_guess_id")
    val no_guess_id: Column<Int> = integer("no_guess_id")

    override val primaryKey = PrimaryKey(question_id, name = "question_id")
}