package no.ssb.kostra.web

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.ssb.kostra.web.viewmodel.KostraFormType

@MicronautTest
class ApiControllerTest(@Client("/api") val client: HttpClient) : BehaviorSpec({

    given("skjema-typer request") {

        val request: HttpRequest<Any> = HttpRequest.GET("/skjematyper")

        `when`("val get request") {
            val httpResponse = withContext(Dispatchers.IO) {
                client.toBlocking().exchange(
                    request, Argument.listOf(KostraFormType::class.java)
                )
            }

            then("response code should be OK") {
                httpResponse.status shouldBe HttpStatus.OK
            }

            and("body should contain data") {
                val formTypes = httpResponse.body()
                formTypes.shouldNotBeNull()
                formTypes.size shouldBe 35

                assertSoftly(formTypes.first { it.id == "0X" }) {
                    it.id shouldBe "0X"
                    it.tittel shouldBe "0X. Resultatregnskap for helsefortak"
                    it.labelOrgnr shouldBe "Organisasjonsnummer for foretaket"
                    it.labelOrgnrVirksomhetene shouldBe "Organisasjonsnummer for virksomhetene"
                }
            }
        }
    }
})
