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

    fun isSecurityDisabled(): Boolean =
        apiKeys().isEmpty()

    fun isValid(key: String?): Boolean =
        key != null && apiKeys().contains(key)
}