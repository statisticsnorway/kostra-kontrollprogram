package no.ssb.kostra.web.service

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import no.ssb.kostra.common.viewmodel.KostraErrorCode
import no.ssb.kostra.common.viewmodel.KostraFormVm

@Ignored("Wait until code is more complete")
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
                errorReport.feilkode shouldBe KostraErrorCode.NORMAL_ERROR
                errorReport.antallKontroller.shouldBeGreaterThan(50)
                errorReport.feil.size shouldBe 1

                assertSoftly(errorReport.feil.first()){
                    feilkode shouldBe KostraErrorCode.NORMAL_ERROR
                }
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

        val PLAIN_TEXT = """
            6676000170201001000101202011119761 1113  1       0302202021                 12122222220302              003000003005      00300521          10120052020
            6676000170201024000101202011119761 1113  1       0302202021                 12 22222220302              003000003005      00300521          10120052020
            """.trimIndent()
    }
}
