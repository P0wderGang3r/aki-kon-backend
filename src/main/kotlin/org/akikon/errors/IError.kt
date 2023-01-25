package org.akikon.errors

interface IError {
    val type: Int
    val text: String
    val response: Map<String, String>
}