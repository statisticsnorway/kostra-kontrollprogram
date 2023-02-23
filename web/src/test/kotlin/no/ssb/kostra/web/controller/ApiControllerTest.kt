package no.ssb.kostra.web.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.validation.validator.Validator
import io.mockk.every
import io.mockk.mockk
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.web.config.UiConfig
import no.ssb.kostra.web.service.DataFileValidator
import no.ssb.kostra.web.viewmodel.KostraFormVm
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.OutputStream
import java.time.Year

class ApiControllerTest : BehaviorSpec({

    lateinit var dataFileValidator: DataFileValidator
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

                every { file.transferTo(any<OutputStream>()) } answers { Mono.just(isValidFileStream) }

                val monoResult = sut.kontrollerSkjema(
                    kostraFormAsJson = objectMapper.writeValueAsString(kostraFormVmInTest),
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
}) {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        private val kostraFormVmInTest = KostraFormVm(
            aar = Year.now().value,
            skjema = "15F",
            region = "667600"
        )
    }
}
