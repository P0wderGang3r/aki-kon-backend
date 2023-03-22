package org.akikon.routes.question

import kotlinx.serialization.decodeFromString
import org.akikon.responses.*
import org.akikon.json.GetQuestion
import org.akikon.jsonParser
import org.akikon.models.Guess
import org.akikon.models.Question
import org.akikon.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun addQuestion(input: String): IResponse {

    //Извлечение идентификатора пользовательской сессии
    val userInput: GetQuestion
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseResponse()
    }

    var transactionStatus: IResponse = DefaultResponse()

    transaction {
        //Получаем идентификатор вопроса
        var questionId: Int = -1
        User.select { User.session eq userInput.session }.forEach {
            questionId = it[User.question_id]
        }
        if (questionId == -1) {
            transactionStatus = SessionResponse()
            return@transaction
        }

        //Получаем имя пользователя
        var currentUsername = ""
        User.select { User.session eq userInput.session }.forEach {
            currentUsername = it[User.username]
        }
        if (currentUsername == "") {
            transactionStatus = SessionResponse()
            return@transaction
        }

        //Ищем идентификатор догадки, которая уже существует
        var newGuessId: Int = -1
        Guess.select { Guess.guess eq userInput.guess }.forEach {
            newGuessId = it[Guess.guess_id]
        }

        //Если идентификатор уже существующей догадки не найден,
        // то создаём новую догадку в БД и получаем её идентификатор
        if (newGuessId == -1) {
            try {
                Guess.insert {
                    it[Guess.guess] = userInput.guess
                }
            } catch (e: Exception) {
                transactionStatus = GuessResponse()
                return@transaction
            }
            Guess.select { Guess.guess eq userInput.guess }.forEach {
                newGuessId = it[Guess.guess_id]
            }
        }

        //Получаем идентификатор альтернативного ответа
        var prevGuessId: Int = -1
        Question.select { Question.question_id eq questionId }.forEach {
            prevGuessId = if (userInput.answer)
                it[Question.yes_guess_id]
            else
                it[Question.no_guess_id]
        }

        //Создаём новый вопрос
        try {
            Question.insert {
                it[Question.username] = currentUsername
                it[Question.question] = userInput.question
                it[Question.yes_guess_id] = if (userInput.guess_answer)
                    newGuessId else prevGuessId
                it[Question.no_guess_id] = if (!userInput.guess_answer)
                    newGuessId else prevGuessId
            }[Question.question_id]
        } catch (e: Exception) {
            Guess.deleteWhere { Guess.guess_id eq newGuessId }
            transactionStatus = QuestionResponse()
            return@transaction
        }

        //Получаем идентификатор нового вопроса
        var newQuestionId: Int = -1
        Question.select {
            (Question.question eq userInput.question) and
                    (Question.username eq currentUsername) and
            (
            ((Question.yes_guess_id eq newGuessId)
                and (Question.no_guess_id eq prevGuessId)) or
            ((Question.yes_guess_id eq prevGuessId)
                and (Question.no_guess_id eq newGuessId))
            )
        }.forEach {
            newQuestionId = it[Question.question_id]
        }
        if (newQuestionId == -1) {
            transactionStatus = DefaultResponse()
            return@transaction
        }

        //Добавляем идентификатор нового вопроса к старому вопросу
        Question.update ({ Question.question_id eq questionId }) {
            if (userInput.answer)
                it[Question.yes_question_id] = newQuestionId
            else
                it[Question.no_question_id] = newQuestionId
        }

        //Сбрасываем номер вопроса пользователя
        User.update({ User.session eq userInput.session }) {
            it[User.question_id] = 1
        }

        transactionStatus = OkResponse(response = mapOf("status" to "Ok"))
    }

    return transactionStatus
}