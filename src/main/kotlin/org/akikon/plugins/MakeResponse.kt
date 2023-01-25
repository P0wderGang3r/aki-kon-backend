package org.akikon.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.akikon.errors.DefaultError
import org.akikon.errors.IError

suspend fun makeResponse(call: ApplicationCall, result: IError) {
    try {
        if (result.type == -1) {
            call.response.status(HttpStatusCode.OK)
        } else {
            call.response.status(HttpStatusCode.BadRequest)
        }
        call.respond(result.response)
    } catch (e: Exception) {
        call.response.status(HttpStatusCode.BadRequest)
        call.respond(DefaultError().response)
    }
}