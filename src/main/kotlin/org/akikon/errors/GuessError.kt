package org.akikon.errors

class GuessError(
    override val type: Int = 4,
    override val text: String = "Сервер: Невозможно прочитать предоставленную догадку (поле: guess - строка).",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IError { }