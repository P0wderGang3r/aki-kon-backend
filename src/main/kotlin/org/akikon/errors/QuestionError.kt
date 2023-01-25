package org.akikon.errors

class QuestionError(
    override val type: Int = 5,
    override val text: String = "Сервер: Невозможно прочитать предоставленный вопрос (поле: question - строка).",
    override val response: Map<String, String> = mapOf("type" to "$type", "text" to text)
) : IError { }