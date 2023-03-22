package org.akikon.responses

class OkResponse(
    override val type: Int = -1,
    override val text: String = "",
    override val response: Map<String, String>
) : IResponse { }