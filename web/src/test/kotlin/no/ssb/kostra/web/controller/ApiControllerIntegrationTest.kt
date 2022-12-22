package no.ssb.kostra.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.ssb.kostra.web.error.ApiError
import no.ssb.kostra.web.error.ApiErrorType
import no.ssb.kostra.web.error.CustomConstraintExceptionHandler.Companion.FALLBACK_PROPERTY_PATH
import no.ssb.kostra.web.viewmodel.CompanyIdVm
import no.ssb.kostra.web.viewmodel.FileReportVm
import no.ssb.kostra.web.viewmodel.KostraFormVm
import no.ssb.kostra.web.viewmodel.UiDataVm
import java.io.File
import java.io.FileWriter
import java.time.Year
import java.util.*

@MicronautTest
class ApiControllerIntegrationTest(
    @Client("/") client: HttpClient,
    objectMapper: ObjectMapper
) : BehaviorSpec({

    given("uiData request") {

        val request: HttpRequest<Any> = HttpRequest.GET("/api/ui-data")

        `when`("valid get request") {
            val httpResponse = withContext(Dispatchers.IO) {
                client.toBlocking().exchange(
                    request, Argument.of(UiDataVm::class.java)
                )
            }

            then("response code should be OK") {
                httpResponse.status shouldBe HttpStatus.OK
            }

            and("body should contain data") {
                val uiData = httpResponse.body()

                uiData.shouldNotBeNull()

                uiData.releaseVersion.length shouldBeGreaterThan 0
                uiData.years.size shouldBeGreaterThan 1

                uiData.formTypes.shouldNotBeNull()
                uiData.formTypes.size shouldBeGreaterThan 30

                assertSoftly(uiData.formTypes.first { it.id == "0X" }) {
                    it.id shouldBe "0X"
                    it.tittel shouldBe "0X. Resultatregnskap for helseforetak"
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
                "invalid aar",
                KostraFormVm(
                    aar = 2020,
                    skjema = "15F",
                    region = "667600"
                ),
                "aar",
                "År kan ikke være mindre enn 2022"
            ),

            row(
                "skjematype = null",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = null,
                    region = "667600"
                ),
                "skjema",
                "Skjematype er påkrevet"
            ),
            row(
                "blank skjematype",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "",
                    region = "667600"
                ),
                "skjema",
                "Skjematype er påkrevet"
            ),
            row(
                "invalid skjematype",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "a",
                    region = "667600"
                ),
                "skjema",
                "Ugyldig skjematype (a)"
            ),

            row(
                "region = null",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = null
                ),
                "region",
                "Region er påkrevet"
            ),
            row(
                "blank region",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = ""
                ),
                "region",
                "Region er påkrevet"
            ),
            row(
                "invalid region",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "a"
                ),
                "region",
                "Region må bestå av 6 siffer uten mellomrom"
            ),

            row(
                "filnavn = null",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "667600",
                    filnavn = null
                ),
                "filnavn",
                "Filvedlegg er påkrevet"
            ),
            row(
                "blank filnavn",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "667600",
                    filnavn = ""
                ),
                "filnavn",
                "Filvedlegg er påkrevet"
            ),

            row(
                "orgnrForetak missing",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0F",
                    region = "667600",
                    filnavn = "test.dat"
                ),
                FALLBACK_PROPERTY_PATH,
                "Skjema krever orgnr"
            ),
            row(
                "invalid orgnrForetak",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0F",
                    region = "667600",
                    orgnrForetak = "a",
                    filnavn = "test.dat"
                ),
                "orgnrForetak",
                "Må starte med 8 eller 9 etterfulgt av 8 siffer"
            ),

            row(
                "orgnrVirksomhet missing",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0X",
                    region = "667600",
                    orgnrForetak = "987654321",
                    filnavn = "test.dat"
                ),
                FALLBACK_PROPERTY_PATH,
                "Skjema krever ett eller flere orgnr for virksomhet(er)"
            ),
            row(
                "Invalid orgnrVirksomhet",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0X",
                    region = "667600",
                    orgnrForetak = "987654321",
                    orgnrVirksomhet = setOf(CompanyIdVm("a")),
                    filnavn = "test.dat"
                ),
                "orgnr",
                "Må starte med 8 eller 9 etterfulgt av 8 siffer"
            )
        ) { description, kostraForm, propertyPath, expectedValidationError ->

            `when`(description) {

                val requestBody = buildMultipartRequest(kostraForm, objectMapper)

                val apiError = shouldThrow<HttpClientResponseException> {
                    client.toBlocking().exchange(
                        HttpRequest.POST(urlInTest, requestBody)
                            .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
                        Any::class.java
                    )
                }.response.getBody(ApiError::class.java).get()

                then("apiError should contain expected values") {
                    assertSoftly(apiError) {
                        errorType shouldBe ApiErrorType.VALIDATION_ERROR
                        httpStatusCode shouldBe HttpStatus.BAD_REQUEST.code
                        url shouldBe urlInTest

                        validationErrors.shouldNotBeNull()
                        @Suppress("USELESS_CAST")
                        (assertSoftly(validationErrors as Map<String, String>) {
                            it[propertyPath] shouldBe expectedValidationError
                        })
                    }
                }
            }
        }
    }

    given("valid POST requests, receive result") {

        val requestBody = buildMultipartRequest(kostraFormInTest, objectMapper)

        `when`("post multipart request") {
            val response = withContext(Dispatchers.IO) {
                client.toBlocking()
                    .exchange(
                        HttpRequest.POST("/api/kontroller-skjema", requestBody)
                            .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
                        FileReportVm::class.java
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
}) {
    companion object {

        private val kostraFormInTest = KostraFormVm(
            aar = Year.now().value,
            skjema = "0G",
            region = "667600",
            orgnrForetak = "987654321",
            filnavn = "0G.dat"
        )

        private val PLAIN_TEXT_0G = """
            0G2020 300500976989732         510  123      263
            0G2020 300500976989732         510           263
        """.trimIndent()

        private fun buildMultipartRequest(
            formData: KostraFormVm,
            objectMapper: ObjectMapper,
            file: File = createTestFile()
        ): MultipartBody {
            return MultipartBody.builder()
                .addPart(
                    "kostraFormAsJson",
                    objectMapper.writeValueAsString(formData)
                )
                .addPart(
                    "file",
                    file.name,
                    MediaType.TEXT_PLAIN_TYPE,
                    file
                ).build()
        }

        private fun createTestFile(): File = File.createTempFile("data", ".dat").apply {
            FileWriter(this).use {
                it.write(PLAIN_TEXT_0G)
            }
        }
    }
}
