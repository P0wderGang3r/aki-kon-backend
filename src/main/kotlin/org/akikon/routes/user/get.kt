package org.akikon.routes.user

import kotlinx.serialization.decodeFromString
import org.akikon.errors.*
import org.akikon.json.GetSession
import org.akikon.jsonParser
import org.akikon.models.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun getUsername(input: String): IError {

    //Извлечение идентификатора пользовательской сессии
    val userInput: GetSession
    try {
        userInput = jsonParser.decodeFromString(input)
    } catch (e: Exception) {
        return ParseError()
    }

    var transactionStatus: IError = DefaultError()

    transaction {
        //Получаем имя пользователя
        var username = ""
        User.select { User.session eq userInput.session }.forEach {
            username = it[User.username]
        }
        if (username == "") {
            transactionStatus = SessionError()
            return@transaction
        }

        transactionStatus = NoError(response = mapOf("username" to username))
    }

    return transactionStatus
}