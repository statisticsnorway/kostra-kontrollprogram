package no.ssb.kostra.web.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.web.viewmodel.KostraFormVm

@MicronautTest
class ControlRunnerTest(
    sut: ControlRunner,
) : BehaviorSpec({
        Given("request with non-fatal error") {
            val kostraForm =
                KostraFormVm(
                    aar = 2025,
                    skjema = "52AF",
                    region = "667200",
                    filnavn = "fil.data",
                )

            When("validateDataFile with valid content") {
                val errorReport =
                    sut.runControls(
                        kostraForm = kostraForm,
                        inputStream = PLAIN_TEXT.toByteArray().inputStream(),
                    )

                Then("errorReportVm should be as expected") {
                    errorReport.severity shouldBe Severity.OK
                    errorReport.antallKontroller.shouldBeGreaterThan(50)
                    errorReport.feil.shouldBeEmpty()
                }
            }

            When("validateDataFile without content") {
                shouldNotThrowAny {
                    sut.runControls(
                        kostraForm = kostraForm,
                        inputStream = "".byteInputStream(),
                    )
                }
            }
        }
    }) {
    companion object {
        private val PLAIN_TEXT =
            """
            6672000170201001000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020
            6672000170201024000101202011119761 1113  1       0302202021                 12 22222220302              003000003005      00300521          10120052020
            """.trimIndent()
    }
}
