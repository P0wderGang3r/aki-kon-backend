package org.akikon.routes.user

import kotlinx.serialization.decodeFromString
import org.akikon.responses.*
import org.akikon.json.NewSession
import org.akikon.jsonParser
import org.akikon.models.User
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.Random

fun newSession(input: String): IResponse {

    //Извлечение имени пользователя и проверка длины имени пользователя
    val userInput: NewSession
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseResponse()
    }

    //Первичное создание ключа пользовательской сессии
    var sessionKey = CharArray(16) { Random.nextInt(48, 90).toChar() }

    var transactionStatus: IResponse = DefaultResponse()

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
                it[question_id] = 1
            }
        } catch (e: Exception) {
            transactionStatus = UsernameResponse()
            return@transaction
        }

        transactionStatus = OkResponse(response = mapOf("session" to String(sessionKey)))
    }

    return transactionStatus
}