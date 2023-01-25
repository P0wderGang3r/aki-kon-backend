package org.akikon.errors

class NoError(
    override val type: Int = -1,
    override val text: String = "",
    override val response: Map<String, String>
) : IError { }