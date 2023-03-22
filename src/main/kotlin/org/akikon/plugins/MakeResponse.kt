package org.akikon.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.akikon.responses.DefaultResponse
import org.akikon.responses.IResponse

suspend fun makeResponse(call: ApplicationCall, result: IResponse) {
    try {
        if (result.type == -1) {
            call.response.status(HttpStatusCode.OK)
        } else {
            call.response.status(HttpStatusCode.BadRequest)
        }
        call.respond(result.response)
    } catch (e: Exception) {
        call.response.status(HttpStatusCode.BadRequest)
        call.respond(DefaultResponse().response)
    }
}