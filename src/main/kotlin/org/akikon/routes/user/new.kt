package org.akikon.routes.user

import kotlinx.serialization.decodeFromString
import org.akikon.errors.*
import org.akikon.json.NewSession
import org.akikon.jsonParser
import org.akikon.models.User
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

fun newSession(input: String): IError {

    //Извлечение имени пользователя и проверка длины имени пользователя
    val userInput: NewSession
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseError()
    }

    //Первичное создание ключа пользовательской сессии
    var sessionKey = CharArray(16) { Random.nextInt(48, 90).toChar() }

    var transactionStatus: IError = DefaultError()

    transaction {

        val query: Query = User.select { User.session eq String(sessionKey) }

        //Если ключ пользовательской сессии уже существует, то генерируем новый
        while (query.count().toInt() != 0) {
            sessionKey = CharArray(16) { Random.nextInt(48, 90).toChar() }
        }

        try {
            User.insert {
                it[username] = userInput.username
                it[session] = String(sessionKey)
                it[question_id] = 0
            }
        } catch (e: Exception) {
            transactionStatus = UsernameError()
            return@transaction
        }

        transactionStatus = NoError(response = mapOf("session" to String(sessionKey)))
    }

    return transactionStatus
}