package org.akikon.routes.user

import kotlinx.serialization.decodeFromString
import org.akikon.responses.*
import org.akikon.json.GetSession
import org.akikon.jsonParser
import org.akikon.models.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun getUsername(input: String): IResponse {

    //Извлечение идентификатора пользовательской сессии
    val userInput: GetSession
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseResponse()
    }

    var transactionStatus: IResponse = DefaultResponse()

    transaction {
        //Получаем имя пользователя
        var username = ""
        User.select { User.session eq userInput.session }.forEach {
            username = it[User.username]
        }
        if (username == "") {
            transactionStatus = SessionResponse()
            return@transaction
        }

        transactionStatus = NoResponse(response = mapOf("username" to username))
    }

    return transactionStatus
}