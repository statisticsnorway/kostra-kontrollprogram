package no.ssb.kostra.web.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.validation.validator.Validator
import io.mockk.every
import io.mockk.mockk
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.service.ControlRunner
import no.ssb.kostra.web.viewmodel.AltinnRequest
import no.ssb.kostra.web.viewmodel.KostraFormVm
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.time.Year
import java.util.*

class ApiControllerTest : BehaviorSpec({

    lateinit var dataFileValidator: ControlRunner
    lateinit var validator: Validator
    lateinit var file: StreamingFileUpload
    lateinit var sut: ApiController

    beforeTest {
        dataFileValidator = mockk(relaxed = true)
        validator = mockk(relaxed = true)

        sut = ApiController(
            UiConfig(),
            dataFileValidator,
            objectMapper,
            validator,
            GitProperties("~tags~")
        )

        file = mockk(relaxed = true)
    }

    Given("kontrollerSkjema") {

        forAll(
            row("valid file stream", true),
            row("invalid file stream", false)
        ) { description, isValidFileStream ->
            When(description) {

                every { file.transferTo(any<OutputStream>()) } answers {
                    Mono.just(
                        isValidFileStream
                    )
                }

                val monoResult = sut.kontrollerSkjema(
                    kostraFormAsJson = objectMapper.writeValueAsString(
                        kostraFormVmInTest
                    ),
                    file = file
                )

                Then("monoResult should be as expected") {
                    if (isValidFileStream) {
                        StepVerifier.create(monoResult)
                            .expectNextCount(1)
                            .verifyComplete()
                    } else {
                        StepVerifier.create(monoResult)
                            .verifyError()
                    }
                }
            }
        }
    }

    Given("kontrollerAltinnSkjema") {

        forAll(
            row(
                "valid request, valid data OK",
                "03030   1   1   1   1   1   1   1   1   1   1   1   1   1   1",
                Severity.OK
            ),
            row(
                "valid request, valid data with errors",
                "XXXXX                                                        ",
                Severity.ERROR
            ),
            row(
                "valid request, invalid data",
                "FATAL",
                Severity.FATAL
            )
        ) { description, data, expectedSeverity ->
            val base64encodedData = Base64
                .getEncoder()
                .encodeToString(
                    data.toByteArray(StandardCharsets.ISO_8859_1)
                )
            val request = altinnRequestInTest.copy(
                base64EncodedFileAttachment = base64encodedData
            )

            When(description) {
                val monoResult = sut.kontrollerAltinnSkjema(request)

                Then("monoResult should be as expected") {
                    StepVerifier.create(monoResult)
                        .expectNextCount(1)
                        .verifyComplete()
                }

                Then("AltinnReport should be as expected") {
                    StepVerifier.create(monoResult)
                        .assertNext { response ->
                            response.shouldNotBeNull()
                            response.body().shouldNotBeNull()

                            val altinnReport = response.body()
                            altinnReport.shouldNotBeNull()
                            altinnReport.severity shouldBe expectedSeverity
                        }
                        .verifyComplete()
                }
            }
        }
    }
}) {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        private val kostraFormVmInTest = KostraFormVm(
            aar = Year.now().value,
            skjema = "15F",
            region = "667600"
        )

        private val altinnRequestInTest = AltinnRequest(
            period = 2026,
            formId = "53F",
            region = "667200",
            base64EncodedFileAttachment = "",
        )
    }
}
