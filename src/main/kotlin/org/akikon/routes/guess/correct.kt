package org.akikon.routes.guess

import kotlinx.serialization.decodeFromString
import org.akikon.errors.*
import org.akikon.json.GetSession
import org.akikon.jsonParser
import org.akikon.models.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun correctGuess(input: String): IError {

    //Извлечение идентификатора пользовательской сессии
    val userInput: GetSession
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseError()
    }

    var transactionStatus: IError = DefaultError()

    transaction {
        //Получаем идентификатор вопроса
        var questionIdentifier: Int = -1
        User.select { User.session eq userInput.session }.forEach {
            questionIdentifier = it[User.question_id]
        }
        if (questionIdentifier == -1) {
            transactionStatus = SessionError()
            return@transaction
        }

        User.update({ User.session eq userInput.session }) {
            it[User.question_id] = 0
        }

        transactionStatus = NoError(response = mapOf("status" to "Ok"))
    }

    return transactionStatus
}