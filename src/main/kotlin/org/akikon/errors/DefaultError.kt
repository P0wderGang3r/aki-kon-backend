package org.akikon.errors

class DefaultError(
    override val type: Int = 0,
    override val text: String = "Сервер: Произошла ошибка при выполнении операции.",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IError { }