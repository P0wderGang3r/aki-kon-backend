package org.akikon.errors

class ParseError(
    override val type: Int = 1,
    override val text: String = "Сервер: Произошла ошибка при попытке сериализации Json.",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IError { }