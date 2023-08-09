package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils

class Rule210InterneOverforingerKjopOgSalgTest : BehaviorSpec({
    Given("context") {
        val sut = Rule210InterneOverforingerKjopOgSalg()

        forAll(
            row(
                "feil regnskap, ikke BevilgningRegnskap",
                listOf(
                    kostraRecordInTest("0X", "100", 0),
                    kostraRecordInTest("0X", "780", -1000)
                ), false
            ),
            row(
                "riktig regnskap, men feil art",
                listOf(
                    kostraRecordInTest("0F", "100", 0),
                    kostraRecordInTest("0F", "710", -1000)
                ), false
            ),
            row(
                "riktig regnskap og art, men sum internKjop + internSalg er utenfor interval på -30",
                listOf(
                    kostraRecordInTest("0F", "380", 0),
                    kostraRecordInTest("0F", "780", -1000)
                ),
                true
            ),
            row(
                "riktig regnskap og art, men sum internKjop + internSalg er utenfor interval på +30",
                listOf(
                    kostraRecordInTest("0F", "380", 1000),
                    kostraRecordInTest("0F", "780", 0)
                ),
                true
            ),
            row(
                "alt riktig, differanse = 0",
                listOf(
                    kostraRecordInTest("0F", "380", 1000),
                    kostraRecordInTest("0F", "780", -1000)
                ), false
            )
        ) { description, kostraRecordList, expectError ->
            val internKjop = kostraRecordList[0].fieldAsIntOrDefault(FIELD_BELOP)
            val internSalg = kostraRecordList[1].fieldAsIntOrDefault(FIELD_BELOP)
            val internDifferanse = internKjop + internSalg

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at differansen ($internDifferanse) mellom " +
                            "internkjøp ($internKjop) og internsalg ($internSalg) stemmer overens " +
                            "(margin på +/- 30')"
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            skjema: String,
            art: String,
            belop: Int,
        ) = RegnskapTestUtils.regnskapRecordInTest(
            mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_ART to art,
                FIELD_BELOP to belop.toString()
            )
        )
    }
}