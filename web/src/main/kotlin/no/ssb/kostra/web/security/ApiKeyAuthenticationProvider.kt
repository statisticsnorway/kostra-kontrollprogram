package no.ssb.kostra.web.security

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.AuthenticationProvider
import jakarta.inject.Singleton

@Singleton
class ApiKeyAuthenticationProvider(
    private val apiKeyConfiguration: ApiKeyConfiguration
) : AuthenticationProvider<HttpRequest<*>, String, String> {
    override fun authenticate(
        request: HttpRequest<*>?,
        authRequest: AuthenticationRequest<String, String>
    ): AuthenticationResponse {

        // No API keys configured: bypass API key security
        if (apiKeyConfiguration.isSecurityDisabled) {
            return AuthenticationResponse.success(
                "anonymous",
                listOf("ROLE_API_USER")
            )
        }

        val apiKey = authRequest.identity

        return if (apiKeyConfiguration.isValid(apiKey)) {
            AuthenticationResponse.success(
                "api-client",
                listOf("ROLE_API_USER")
            )
        } else {
            AuthenticationResponse.failure(
                AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH
            )
        }
    }
}