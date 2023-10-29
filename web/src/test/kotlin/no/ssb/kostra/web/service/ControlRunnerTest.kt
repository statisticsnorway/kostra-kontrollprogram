package no.ssb.kostra.web.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.web.viewmodel.KostraFormVm

class ControlRunnerTest : BehaviorSpec({
    val sut = ControlRunner()

    Given("request with non-fatal error") {
        val kostraForm = KostraFormVm(
            aar = 2022,
            skjema = "52AF",
            region = "667600",
            filnavn = "fil.data",
            orgnrVirksomhet = emptyList()
        )

        When("validateDataFile with valid content") {
            val errorReport = sut.runControls(
                kostraForm = kostraForm,
                inputStream = PLAIN_TEXT.toByteArray().inputStream()
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
                    kostraForm,
                    "".byteInputStream()
                )
            }
        }
    }
}) {
    companion object {
        private val PLAIN_TEXT = """
            6676000170201001000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020
            6676000170201024000101202011119761 1113  1       0302202021                 12 22222220302              003000003005      00300521          10120052020
            """.trimIndent()
    }
}
