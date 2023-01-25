package org.akikon

import kotlinx.serialization.json.Json

public val jsonParser = Json {
    ignoreUnknownKeys = true
}