package org.akikon.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object User: Table() {
    val user_id: Column<Int> = integer("user_id").uniqueIndex().autoIncrement()
    val username: Column<String> = varchar("username", 30)
    val session: Column<String> = varchar("session", 16).uniqueIndex()
    val question_id: Column<Int> = integer("question_id")

    override val primaryKey = PrimaryKey(user_id, name = "user_id")
}