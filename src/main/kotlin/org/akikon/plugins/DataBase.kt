package org.akikon.plugins

import io.ktor.server.application.*
import org.akikon.models.Guess
import org.akikon.models.Question
import org.akikon.models.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
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

    var isNewlyCreated = false

    try {
        if (!Files.exists(Paths.get(filePath))) {
            Files.createFile(Paths.get(filePath))
            isNewlyCreated = true
        }
    } catch (e: Exception) {
        println("Trouble in creation of file")
        return false
    }

    Database.connect("jdbc:sqlite:./data/data.db", "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create(User, Question, Guess)
    }

    if (isNewlyCreated) {
        transaction {
            Guess.insert {
                it[Guess.guess] = "кот"
            }
            Guess.insert {
                it[Guess.guess] = "кит"
            }
            Question.insert {
                it[Question.username] = "Администратор"
                it[Question.question] = "живёт на суше"
                it[Question.yes_guess_id] = 1
                it[Question.no_guess_id] = 2
            }[Question.question_id]
        }
    }

    return true
}