package no.ssb.kostra.web.error

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import no.ssb.kostra.web.viewmodel.ReportRequestVm
import java.util.*

@MicronautTest
class CustomConstraintExceptionHandlerTest(@Client("/") val client: HttpClient) : BehaviorSpec({

    given("request body with invalid values, receive ApiError") {
        val urlInTest = "/api/validate"

        val requestBody = ReportRequestVm(
            aar = 2020,
            skjema = "52AF",
            region = "667600",
            organisasjon = "UOPPGITT",
            base64EncodedContent = null
        )

        `when`("POST with invalid data") {
            val apiError = shouldThrow<HttpClientResponseException> {
                client.toBlocking().exchange(HttpRequest.POST(urlInTest, requestBody), Any::class.java)
            }.response.getBody(ApiError::class.java).get()

            then("apiError should contain expected values") {
                assertSoftly(apiError) {
                    errorType shouldBe ApiErrorType.VALIDATION_ERROR
                    httpStatusCode shouldBe HttpStatus.BAD_REQUEST.code
                    url shouldBe urlInTest

                    validationErrors.shouldNotBeNull()
                    @Suppress("USELESS_CAST")
                    assertSoftly(validationErrors as Map<String, String>) {
                        it.size shouldBe 2
                        it["aar"] shouldBe "must be greater than or equal to 2021"
                    }
                }
            }
        }
    }
})
