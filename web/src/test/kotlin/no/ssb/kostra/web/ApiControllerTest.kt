package no.ssb.kostra.web

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.ssb.kostra.web.error.ApiError
import no.ssb.kostra.web.error.ApiErrorType
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.ErrorReportVm
import no.ssb.kostra.web.viewmodel.KostraFormTypeVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import java.util.*

@Suppress("unused")
@MicronautTest
class ApiControllerTest(@Client("/") val client: HttpClient) : BehaviorSpec({

    given("skjematyper request") {

        val request: HttpRequest<Any> = HttpRequest.GET("/api/skjematyper")

        `when`("valid get request") {
            val httpResponse = withContext(Dispatchers.IO) {
                client.toBlocking().exchange(
                    request, Argument.listOf(KostraFormTypeVm::class.java)
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

    given("invalid POST requests, receive ApiError") {
        val urlInTest = "/api/kontroller-skjema"

        forAll(
            row(
                "Invalid aar",
                KostraFormVm(
                    aar = 2020,
                    skjema = "15F",
                    region = "667600"
                ),
                "aar",
                "År kan ikke være mindre enn 2021"
            ),
            row(
                "Blank skjematype",
                KostraFormVm(
                    aar = 2022,
                    skjema = "",
                    region = "667600"
                ),
                "skjema",
                "Skjematype må være utfylt"
            ),
            row(
                "Invalid skjematype",
                KostraFormVm(
                    aar = 2022,
                    skjema = "a",
                    region = "667600"
                ),
                "skjema",
                "Ugyldig skjematype (a)"
            ),
            row(
                "Blank region",
                KostraFormVm(
                    aar = 2022,
                    skjema = "15F",
                    region = ""
                ),
                "region",
                "Region må bestå av 6 siffer uten mellomrom"
            ),
            row(
                "Invalid region",
                KostraFormVm(
                    aar = 2022,
                    skjema = "15F",
                    region = "a"
                ),
                "region",
                "Region må bestå av 6 siffer uten mellomrom"
            ),
            row(
                "base64EncodedContent missing",
                KostraFormVm(
                    aar = 2022,
                    skjema = "15F",
                    region = "667600"
                ),
                "base64EncodedContent",
                "Filvedlegg mangler"
            ),
            row(
                "orgnrForetak missing",
                KostraFormVm(
                    aar = 2022,
                    skjema = "0F",
                    region = "667600",
                    base64EncodedContent = base64EncodedContent
                ),
                "kostraForm",
                "Skjema krever orgnr"
            ),
            row(
                "Invalid orgnrForetak",
                KostraFormVm(
                    aar = 2022,
                    skjema = "0F",
                    region = "667600",
                    orgnrForetak = CompanyIdVm("a"),
                    base64EncodedContent = base64EncodedContent
                ),
                "orgnr",
                "Orgnr må bestå av 9 siffer uten mellomrom"
            ),
            row(
                "orgnrVirksomhet missing",
                KostraFormVm(
                    aar = 2022,
                    skjema = "0X",
                    region = "667600",
                    orgnrForetak = CompanyIdVm("987654321"),
                    base64EncodedContent = base64EncodedContent
                ),
                "kostraForm",
                "Skjema krever ett eller flere orgnr for virksomhet(er)"
            ),
            row(
                "Invalid orgnrVirksomhet",
                KostraFormVm(
                    aar = 2022,
                    skjema = "0X",
                    region = "667600",
                    orgnrForetak = CompanyIdVm("987654321"),
                    orgnrVirksomhet = setOf(CompanyIdVm("a")),
                    base64EncodedContent = base64EncodedContent
                ),
                "orgnr",
                "Orgnr må bestå av 9 siffer uten mellomrom"
            )
        ) { description, requestBody, validationKey, expectedValidationError ->

            `when`(description) {
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
                        (assertSoftly(validationErrors as Map<String, String>) {
                            it[validationKey] shouldBe expectedValidationError
                        })
                    }
                }
            }
        }
    }

    given("valid POST requests, receive result") {

        forAll(
            row(
                "Valid orgnrVirksomhet",
                KostraFormVm(
                    aar = 2022,
                    skjema = "0G",
                    region = "667600",
                    orgnrForetak = CompanyIdVm("987654321"),
                    base64EncodedContent = base64EncodedContent
                )
            )
        ) { description, requestBody ->

            `when`(description) {
                val response = withContext(Dispatchers.IO) {
                    client.toBlocking()
                        .exchange(
                            HttpRequest.POST("/api/kontroller-skjema", requestBody),
                            ErrorReportVm::class.java
                        )
                }

                then("status should be OK") {
                    response.status shouldBe HttpStatus.OK
                }

                and("error report should contain expected values") {
                    assertSoftly(response.body()!!) {
                        it.antallKontroller shouldBe 2
                    }
                }
            }
        }
    }
}) {
    companion object {

        private val PLAIN_TEXT_0G = """
            0G2020 300500976989732         510  123      263
            0G2020 300500976989732         510           263
        """.trimIndent()

        private val base64EncodedContent: String = Base64.getEncoder().encodeToString(PLAIN_TEXT_0G.toByteArray())
    }
}
