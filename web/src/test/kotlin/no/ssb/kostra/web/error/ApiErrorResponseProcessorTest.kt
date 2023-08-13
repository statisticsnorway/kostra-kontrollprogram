package no.ssb.kostra.web.error

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class ApiErrorResponseProcessorTest(@Client("/") val client: HttpClient) : BehaviorSpec({

    Given("ApiErrorResponseProcessor") {

        forAll(
            row("non-existing URL", "/non-existing", HttpStatus.NOT_FOUND),
            row("URL that throws exception", "/api/kontroller-skjema", HttpStatus.METHOD_NOT_ALLOWED),
        ) { description, url, expectedHttpStatus ->

            When(description) {

                val exception = shouldThrow<HttpClientResponseException> {
                    client.toBlocking().exchange<Any>(url)
                }

                Then("receive API error") {
                    exception.status shouldBe expectedHttpStatus

                    assertSoftly(exception.response.getBody(ApiError::class.java).get()) {
                        errorType shouldBe ApiErrorType.SYSTEM_ERROR
                        httpStatusCode shouldBe expectedHttpStatus.code
                        url shouldBe url
                    }
                }
            }
        }
    }
})
