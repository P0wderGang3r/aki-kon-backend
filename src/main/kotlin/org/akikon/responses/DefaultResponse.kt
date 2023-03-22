package org.akikon.responses

class DefaultResponse(
    override val type: Int = 0,
    override val text: String = "Сервер: Произошла ошибка при выполнении операции.",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IResponse { }