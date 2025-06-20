package no.ssb.kostra.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.json.schema.shouldMatchSchema
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
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
import no.ssb.kostra.web.controller.KoTestJsonSchemas.rapportSchema
import no.ssb.kostra.web.error.ApiError
import no.ssb.kostra.web.error.ApiErrorType
import no.ssb.kostra.web.error.CustomConstraintExceptionHandler.Companion.FALLBACK_PROPERTY_PATH
import no.ssb.kostra.web.viewmodel.KostraFormVm
import no.ssb.kostra.web.viewmodel.UiDataVm
import java.io.File
import java.io.FileWriter
import java.time.Year

@OptIn(ExperimentalKotest::class)
@MicronautTest
class ApiControllerIntegrationTest(
    @Client("/") client: HttpClient,
    objectMapper: ObjectMapper,
) : BehaviorSpec({

    Given("uiData request") {
        val request: HttpRequest<Any> = HttpRequest.GET("/api/ui-data")

        When("valid get request") {
            val httpResponse =
                client.toBlocking().exchange(
                    request,
                    Argument.of(UiDataVm::class.java),
                )

            Then("response code should be OK") {
                httpResponse.status shouldBe HttpStatus.OK
            }

            And("body should contain data") {
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
                }
            }
        }
    }

    Given("invalid POST requests, receive ApiError") {
        val urlInTest = "/api/kontroller-skjema"

        forAll(
            row(
                "invalid aar",
                KostraFormVm(
                    aar = 2020,
                    skjema = "15F",
                    region = "667600",
                ),
                "aar",
                "År kan ikke være mindre enn 2022",
            ),
            row(
                "blank skjematype",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "",
                    region = "667600",
                ),
                "skjema",
                "Ugyldig skjematype ()",
            ),
            row(
                "white-space skjematype",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "   ",
                    region = "667600",
                ),
                "skjema",
                "Ugyldig skjematype (   )",
            ),
            row(
                "invalid skjematype",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "a",
                    region = "667600",
                ),
                "skjema",
                "Ugyldig skjematype (a)",
            ),
            row(
                "empty region",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "",
                ),
                "region",
                "Region må bestå av 6 siffer uten mellomrom",
            ),
            row(
                "blank region",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "   ",
                ),
                "region",
                "Region må bestå av 6 siffer uten mellomrom",
            ),
            row(
                "invalid region",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "a",
                ),
                "region",
                "Region må bestå av 6 siffer uten mellomrom",
            ),
            row(
                "empty filnavn",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "667600",
                    filnavn = "",
                ),
                "filnavn",
                "Filvedlegg er påkrevet",
            ),
            row(
                "blank filnavn",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "15F",
                    region = "667600",
                    filnavn = "  ",
                ),
                "filnavn",
                "Filvedlegg er påkrevet",
            ),
            row(
                "orgnrForetak missing",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0F",
                    region = "667600",
                    filnavn = "test.dat",
                ),
                FALLBACK_PROPERTY_PATH,
                "Skjema krever orgnr",
            ),
            row(
                "empty orgnrForetak",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0F",
                    region = "667600",
                    orgnrForetak = "",
                    filnavn = "test.dat",
                ),
                "orgnrForetak",
                "Må starte med 8 eller 9 etterfulgt av 8 siffer",
            ),
            row(
                "blank orgnrForetak",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0F",
                    region = "667600",
                    orgnrForetak = "  ",
                    filnavn = "test.dat",
                ),
                "orgnrForetak",
                "Må starte med 8 eller 9 etterfulgt av 8 siffer",
            ),
            row(
                "invalid orgnrForetak",
                KostraFormVm(
                    aar = Year.now().value,
                    skjema = "0X",
                    region = "667600",
                    orgnrForetak = "a",
                    filnavn = "test.dat",
                ),
                "orgnrForetak",
                "Må starte med 8 eller 9 etterfulgt av 8 siffer",
            ),
        ) { description, kostraForm, propertyPath, expectedValidationError ->
            When(description) {
                val requestBody = buildMultipartRequest(kostraForm, objectMapper)

                val apiError =
                    shouldThrow<HttpClientResponseException> {
                        client.toBlocking().exchange(
                            HttpRequest
                                .POST(urlInTest, requestBody)
                                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
                            Any::class.java,
                        )
                    }.response.getBody(ApiError::class.java).get()

                Then("apiError should contain expected values") {
                    assertSoftly(apiError) {
                        errorType shouldBe ApiErrorType.VALIDATION_ERROR
                        httpStatusCode shouldBe HttpStatus.BAD_REQUEST.code
                        url shouldBe urlInTest
                        validationErrors.shouldNotBeNull()

                        assertSoftly(validationErrors as Map<String, String>) {
                            it[propertyPath] shouldBe expectedValidationError
                        }
                    }
                }
            }
        }
    }

    Given("valid POST requests, receive result") {
        val requestBody = buildMultipartRequest(kostraFormInTest, objectMapper)

        When("post multipart request") {
            val response =
                client.toBlocking().exchange(
                    HttpRequest
                        .POST("/api/kontroller-skjema", requestBody)
                        .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
                    String::class.java,
                )

            Then("status should be OK") {
                response.status shouldBe HttpStatus.OK
            }

            And("error report should contain expected values") {
                response.body()
                    .shouldNotBeNull()
                    .shouldMatchSchema(rapportSchema)
            }
        }
    }
}) {
    companion object {
        private val kostraFormInTest =
            KostraFormVm(
                aar = Year.now().value,
                skjema = "0G",
                region = "667600",
                orgnrForetak = "987654321",
                filnavn = "0G.dat",
            )

        private val PLAIN_TEXT_0G =
            """
            0G2020 300500976989732         510  123      263
            0G2020 300500976989732         510           263
            """.trimIndent()

        private fun buildMultipartRequest(
            formData: KostraFormVm,
            objectMapper: ObjectMapper,
        ): MultipartBody =
            createTestFile().let { file ->
                MultipartBody
                    .builder()
                    .addPart(
                        "kostraFormAsJson",
                        objectMapper.writeValueAsString(formData),
                    ).addPart(
                        "file",
                        file.name,
                        MediaType.TEXT_PLAIN_TYPE,
                        file,
                    ).build()
            }

        private fun createTestFile(): File =
            File.createTempFile("data", ".dat").apply {
                FileWriter(this).use {
                    it.write(PLAIN_TEXT_0G)
                }
            }
    }
}
