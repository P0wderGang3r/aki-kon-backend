package org.akikon.plugins

import io.ktor.server.application.*
import org.akikon.models.Guess
import org.akikon.models.Question
import org.akikon.models.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Paths

fun Application.connectDataBase(): Boolean {
    //Creating directory
    val dirPath = "./data"

    try {
        if (!Files.exists(Paths.get(dirPath))) {
            Files.createDirectory(Paths.get(dirPath))
        }
    } catch (e: Exception) {
        println("Trouble in creation of directory")
        return false
    }

    //Creating file
    val filePath = "./data/data.db"

    try {
        if (!Files.exists(Paths.get(filePath))) {
            Files.createFile(Paths.get(filePath))
        }
    } catch (e: Exception) {
        println("Trouble in creation of file")
        return false
    }

    Database.connect("jdbc:sqlite:./data/data.db", "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create(User, Question, Guess)
    }

    return true
}