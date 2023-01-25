package org.akikon.json

import kotlinx.serialization.Serializable

@Serializable
class GetAnswer(val session: String, val answer: Boolean) {
}