package no.ssb.kostra.altinn.security

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("api-keys")
class ApiKeyConfiguration {

    var allowed: String = ""

    fun apiKeys(): Set<String> =
        allowed
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toSet()

    fun isValid(key: String?): Boolean = apiKeys().any { it == key?.trim() }
}