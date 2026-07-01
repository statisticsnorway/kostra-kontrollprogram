package no.ssb.kostra.altinn.controller

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import no.ssb.kostra.altinn.model.AltinnRequest
import no.ssb.kostra.altinn.model.AltinnRespondent
import java.util.Base64

@Property(name = "api-keys.allowed", value = "valid-api-key")
@MicronautTest
class ApiControllerIntegrationTest(
    @param:Client("/") private val httpClient: HttpClient,
) : BehaviorSpec({

    Given("an API secured by API key") {
        val client = httpClient.toBlocking()

        When("no API key is provided") {
            val request = HttpRequest
                .POST<Any?>("/api/kontroller-altinn-skjema", null)
                .accept(MediaType.APPLICATION_JSON)
            val thrown = shouldThrow<HttpClientResponseException> {
                client.exchange(request, String::class.java)
            }

            Then("it returns HTTP 401 Unauthorized") {
                thrown.status shouldBe HttpStatus.UNAUTHORIZED
            }
        }

        When("an invalid API key is provided") {
            val thrown = shouldThrow<HttpClientResponseException> {
                client.exchange(createRequest("invalid-api-key"), String::class.java)
            }

            Then("it returns HTTP 401 Unauthorized") {
                thrown.status shouldBe HttpStatus.UNAUTHORIZED
            }
        }

        When("a valid API key is provided with a valid payload") {
            val response = shouldNotThrowAny {
                client.exchange(createRequest("valid-api-key"), String::class.java)
            }

            Then("it returns HTTP 200 OK") {
                response.status shouldBe HttpStatus.OK
            }
        }

        When("a valid API key is provided with an invalid payload") {
            val thrown = shouldThrow<HttpClientResponseException> {
                client.exchange(createBadRequest("valid-api-key"), String::class.java)
            }

            Then("it returns HTTP 400 Bad Request") {
                thrown.status shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }
}) {
    companion object {
        private val validBase64Data: String = Base64.getEncoder()
            .encodeToString(
                "03030   1   1   1   1   1   1   1   1   1   1   1   1   1   1"
                    .toByteArray(Charsets.ISO_8859_1)
            )

        private fun createRequest(apiKey: String) = HttpRequest.POST<Any?>(
            "/api/kontroller-altinn-skjema",
            AltinnRequest(
                respondent = AltinnRespondent(
                    aar = 2026,
                    skjema = "53F",
                    region = "667200",
                ),
                base64KodedeData = validBase64Data,
            )
        )
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", apiKey)

        private fun createBadRequest(apiKey: String) = HttpRequest.POST<Any?>(
            "/api/kontroller-altinn-skjema",
            """{"will": "fail"}"""
        )
            .accept(MediaType.APPLICATION_JSON)
            .header("X-API-KEY", apiKey)
    }
}