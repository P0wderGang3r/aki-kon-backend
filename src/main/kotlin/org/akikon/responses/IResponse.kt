package org.akikon.responses

interface IResponse {
    val type: Int
    val text: String
    val response: Map<String, String>
}