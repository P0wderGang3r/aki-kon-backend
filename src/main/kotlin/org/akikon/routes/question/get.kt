package org.akikon.routes.question

import kotlinx.serialization.decodeFromString
import org.akikon.responses.*
import org.akikon.json.GetSession
import org.akikon.jsonParser
import org.akikon.models.Question
import org.akikon.models.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun getQuestion(input: String): IResponse {

    //Извлечение идентификатора пользовательской сессии
    val userInput: GetSession
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseResponse()
    }

    var transactionStatus: IResponse = DefaultResponse()

    transaction {
        //Получаем идентификатор вопроса
        var questionIdentifier: Int = -1
        User.select { User.session eq userInput.session }.forEach {
            questionIdentifier = it[User.question_id]
        }
        if (questionIdentifier == -1) {
            transactionStatus = SessionResponse()
            return@transaction
        }

        //Получаем текст вопроса
        var questionText = ""
        Question.select { Question.question_id eq questionIdentifier }.forEach {
            questionText = it[Question.question]
        }

        transactionStatus = NoResponse(response = mapOf("question" to questionText))
    }

    return transactionStatus
}