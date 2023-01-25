package org.akikon.json

import kotlinx.serialization.Serializable

@Serializable
class GetQuestion(
    val session: String,
    val question: String,
    val answer: Boolean,
    val guess_answer: Boolean,
    val guess: String) {
}