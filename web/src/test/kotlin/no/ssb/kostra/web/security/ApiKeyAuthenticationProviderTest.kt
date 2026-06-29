package no.ssb.kostra.web.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationRequest
import io.mockk.every
import io.mockk.mockk

class ApiKeyAuthenticationProviderTest : StringSpec({

    val apiKeyConfiguration = mockk<ApiKeyConfiguration>()
    val provider = ApiKeyAuthenticationProvider(apiKeyConfiguration)
    val httpRequest = mockk<HttpRequest<*>>(relaxed = true)
    val authRequest = mockk<AuthenticationRequest<String, String>>()

    "should bypass security if disabled" {
        every { apiKeyConfiguration.isSecurityDisabled } returns true

        val response = provider.authenticate(httpRequest, authRequest)

        response.isAuthenticated shouldBe true
    }

    "should authenticate with valid api key" {
        every { apiKeyConfiguration.isSecurityDisabled } returns false
        every { authRequest.identity } returns "valid-key"
        every { apiKeyConfiguration.isValid("valid-key") } returns true

        val response = provider.authenticate(httpRequest, authRequest)

        response.isAuthenticated shouldBe true
    }

    "should fail authentication with invalid api key" {
        every { apiKeyConfiguration.isSecurityDisabled } returns false
        every { authRequest.identity } returns "invalid-key"
        every { apiKeyConfiguration.isValid("invalid-key") } returns false

        val response = provider.authenticate(httpRequest, authRequest)

        response.isAuthenticated shouldBe false
    }
})
