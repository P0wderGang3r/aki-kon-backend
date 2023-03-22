package org.akikon.responses

class UsernameResponse(
    override val type: Int = 3,
    override val text: String = "Сервер: Невозможно прочитать предоставленное имя пользователя (поле: username - строка).",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IResponse { }