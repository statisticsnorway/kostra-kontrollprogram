package no.ssb.kostra.web.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import io.mockk.every
import io.mockk.mockk

class ApiKeyFilterTest : StringSpec({
    val apiKeyConfiguration = mockk<ApiKeyConfiguration>()
    val filter = ApiKeyFilter(apiKeyConfiguration)
    val chain = mockk<ServerFilterChain>()
    val request = mockk<HttpRequest<Any>>()
    val responseMock = mockk<MutableHttpResponse<*>>()
    val headersMock = mockk<io.micronaut.http.HttpHeaders>(relaxed = true)

    "should proceed if security is disabled" {
        every { apiKeyConfiguration.isSecurityDisabled } returns true
        every { request.headers } returns headersMock
        every { chain.proceed(request) } returns Mono.just(responseMock)

        val result = filter.doFilter(request, chain)
        val response = (result as Mono<*>).block()
        println("Response type: ${response?.javaClass}")
        response shouldBe responseMock
    }

    "should proceed if api key is valid" {
        every { apiKeyConfiguration.isSecurityDisabled } returns false
        every { request.headers } returns headersMock
        every { headersMock["X-API-Key"] } returns "valid-key"
        every { apiKeyConfiguration.isValid("valid-key") } returns true
        every { chain.proceed(request) } returns Mono.just(responseMock)

        val result = filter.doFilter(request, chain)
        val response = (result as Mono<*>).block()
        response shouldBe responseMock
    }

    "should return unauthorized if api key is missing or invalid" {
        every { apiKeyConfiguration.isSecurityDisabled } returns false
        every { request.headers } returns headersMock
        every { headersMock["X-API-Key"] } returns null
        every { apiKeyConfiguration.isValid(null) } returns false

        val result = filter.doFilter(request, chain)
        val response = (result as Mono<MutableHttpResponse<*>>).block()
        response!!.status shouldBe HttpStatus.UNAUTHORIZED
    }

    "should return unauthorized if api key is present but invalid" {
        every { apiKeyConfiguration.isSecurityDisabled } returns false
        every { request.headers } returns headersMock
        every { headersMock["X-API-Key"] } returns "invalid-key"
        every { apiKeyConfiguration.isValid("invalid-key") } returns false

        val result = filter.doFilter(request, chain)
        val response = (result as Mono<MutableHttpResponse<*>>).block()
        response!!.status shouldBe HttpStatus.UNAUTHORIZED
    }
})
