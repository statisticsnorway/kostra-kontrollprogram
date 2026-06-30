package no.ssb.kostra.altinn.controller

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import jakarta.inject.Inject
import no.ssb.kostra.altinn.model.AltinnRequest
import no.ssb.kostra.altinn.model.AltinnRespondent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.api.function.ThrowingSupplier


@Property(name = "api-keys.allowed", value = "secret")
@MicronautTest
class ApiControllerIntegrationTest {
    @Inject
    @Client("/")
    var httpClient: HttpClient? = null

    @Test
    fun apiIsSecured() {
        val client = httpClient!!.toBlocking()
        val request: HttpRequest<*>? =
            HttpRequest.GET<Any?>("/api").accept(MediaType.APPLICATION_JSON)
        val e = Executable { client.exchange(request, String::class.java) }
        val thrown: HttpClientResponseException =
            Assertions.assertThrows<HttpClientResponseException?>(
                HttpClientResponseException::class.java,
                e
            )!!
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, thrown.status)
    }

    @Test
    fun apiNotAccessibleIfWrongKey() {
        val client = httpClient!!.toBlocking()
        val request = createRequest("FAIL")
        val e = Executable { client.exchange(request, String::class.java) }
        val thrown: HttpClientResponseException =
            Assertions.assertThrows<HttpClientResponseException?>(
                HttpClientResponseException::class.java,
                e
            )!!
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, thrown.status)
    }

    @Test
    fun apiIsAccessibleWithAnApiKey() {
        val client = httpClient!!.toBlocking()

        var response: HttpResponse<String?> =
            Assertions.assertDoesNotThrow<HttpResponse<String?>?>(
                ThrowingSupplier {
                    client.exchange(
                        createRequest("secret"),
                        String::class.java
                    )
                })!!
        Assertions.assertEquals(HttpStatus.OK, response.getStatus())
    }

    companion object {
        private fun createRequest(apiKey: String?): HttpRequest<*>? {
            val altinnRequestInTest = AltinnRequest(
                respondent = AltinnRespondent(
                    aar = 2026,
                    skjema = "53F",
                    region = "667200",
                ),
                base64KodedeData = "",
            )

            return HttpRequest.GET<Any?>("/api")
                .accept(MediaType.TEXT_PLAIN)
                .header("X-API-KEY", apiKey)
                .body(altinnRequestInTest)
        }

        private fun createBadRequest(apiKey: String?): HttpRequest<*>? {
            return HttpRequest.GET<Any?>("/api")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", apiKey)
                .body("This is a bad request")
        }

    }
}
