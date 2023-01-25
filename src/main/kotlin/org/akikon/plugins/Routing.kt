package org.akikon.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.akikon.routes.guess.correctGuess
import org.akikon.routes.question.addQuestion
import org.akikon.routes.question.answerQuestion
import org.akikon.routes.question.getQuestion
import org.akikon.routes.user.getUsername
import org.akikon.routes.user.newSession


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Ok")
        }

        post("/user/get") {
            makeResponse(call, getUsername(call.receiveText()))
        }

        post("/user/new") {
            makeResponse(call, newSession(call.receiveText()))
        }

        post("/question/get") {
            makeResponse(call, getQuestion(call.receiveText()))
        }

        post("/question/answer") {
            makeResponse(call, answerQuestion(call.receiveText()))
        }

        post("/question/add") {
            makeResponse(call, addQuestion(call.receiveText()))
        }

        post("/guess/correct") {
            makeResponse(call, correctGuess(call.receiveText()))
        }

    }
}
