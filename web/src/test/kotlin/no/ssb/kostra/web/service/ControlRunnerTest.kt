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

                Then("errorReport.severity should be OK") {
                    errorReport.severity shouldBe Severity.OK
                }

                Then("errorReport.antallKontroller should be greater than 50") {
                    errorReport.antallKontroller.shouldBeGreaterThan(50)
                }

                Then("errorReport.feil should be empty") {
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
            667600203020103600010120251111976121113  1       0302202521                 12122222220302              003000003005      00300521          10120052025
            667600205020103700010120251111976121113  1       0302202521                 12122222220302              003000003005      00300521          10120052025
            """.trimIndent()
    }
}
