package no.ssb.kostra.web.security

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("api-keys")
class ApiKeyConfiguration {

    var allowed: String = ""

    val apiKeys: Set<String>
        get() = allowed
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toSet()

    val isSecurityDisabled: Boolean
        get() = apiKeys.isEmpty()

    fun isValid(key: String?): Boolean = key != null && apiKeys.contains(key)
}