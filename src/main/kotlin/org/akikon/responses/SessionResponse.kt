package org.akikon.responses

class SessionResponse(
    override val type: Int = 2,
    override val text: String = "Сервер: Невозможно найти пользователя, соответствующего предоставленному идентификатору сессии (поле: session - строка).",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IResponse { }