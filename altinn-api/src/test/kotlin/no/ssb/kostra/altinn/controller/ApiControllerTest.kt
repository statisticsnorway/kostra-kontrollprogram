package no.ssb.kostra.altinn.controller

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.altinn.model.AltinnRequest
import no.ssb.kostra.altinn.model.AltinnRespondent
import no.ssb.kostra.felles.git.GitProperties
import no.ssb.kostra.validation.report.Severity
import reactor.test.StepVerifier
import java.nio.charset.StandardCharsets
import java.util.*

class ApiControllerTest : BehaviorSpec({

    lateinit var sut: ApiController

    beforeTest {
        sut = ApiController(
            GitProperties("~tags~")
        )
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
                base64KodedeData = base64encodedData
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
                            altinnReport.alvorlighetsgrad shouldBe expectedSeverity.info.description
                        }
                        .verifyComplete()
                }
            }
        }
    }
}) {
    companion object {
        private val altinnRequestInTest = AltinnRequest(
            respondent = AltinnRespondent(
                aar = 2026,
                skjema = "53F",
                region = "667200",
            ),
            base64KodedeData = "",
        )
    }
}
