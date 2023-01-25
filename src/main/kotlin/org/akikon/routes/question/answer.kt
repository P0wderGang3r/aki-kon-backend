package org.akikon.routes.question

import kotlinx.serialization.decodeFromString
import org.akikon.errors.*
import org.akikon.json.GetAnswer
import org.akikon.jsonParser
import org.akikon.models.Guess
import org.akikon.models.Question
import org.akikon.models.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun answerQuestion(input: String): IError {

    //Извлечение идентификатора пользовательской сессии
    val userInput: GetAnswer
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

        //Получаем ветвление по выбранному пути на заданный вопрос
        var newQuestionId: Int? = null
        Question.select { Question.question_id eq questionIdentifier }.forEach {
            newQuestionId = if (userInput.answer)
                it[Question.yes_question_id]
            else
                it[Question.no_question_id]
        }

        if (newQuestionId != null) {
            User.update({ User.session eq userInput.session }) {
                it[User.question_id] = newQuestionId!!
            }
            transactionStatus = NoError(response = mapOf("answer_type" to "1", "guess" to ""))
            return@transaction
        }

        //Если по данному ветвлению не имеем следующего вопроса, то делаем предположение
        var guessId: Int = -1
        var guess = ""

        //Получаем идентификатор догадки по выбранному пути на заданный вопрос
        Question.select { Question.question_id eq questionIdentifier }.forEach {
            if (userInput.answer)
                guessId = it[Question.yes_guess_id]
            else
                guessId = it[Question.no_guess_id]
        }

        //Получаем текст догадки
        Guess.select { Guess.guess_id eq guessId }.forEach {
            guess = it[Guess.guess]
        }

        transactionStatus = NoError(response = mapOf("answer_type" to "0", "guess" to guess))
    }

    return transactionStatus
}