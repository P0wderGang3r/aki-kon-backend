package org.akikon

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.utils.io.errors.*
import org.akikon.plugins.configureHTTP
import org.akikon.plugins.configureRouting
import org.akikon.plugins.configureSerialization
import org.akikon.plugins.connectDataBase

fun main() {

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    if (!connectDataBase())
        throw (IOException())

    configureSerialization()
    configureHTTP()
    configureRouting()
}