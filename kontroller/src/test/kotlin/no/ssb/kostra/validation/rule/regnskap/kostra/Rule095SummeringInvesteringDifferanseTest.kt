package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils

class Rule095SummeringInvesteringDifferanseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule095SummeringInvesteringDifferanse()

        forAll(
            row(
                "feil skjema",
                listOf(
                    kostraRecordInTest("XX", "XXXXXX", "0","100", 0),
                    kostraRecordInTest("XX", "XXXXXX", "0", "780", -1000)
                ), false
            ),
            row(
                "riktig skjema og regnskap, men Oslo bydel",
                listOf(
                    kostraRecordInTest("0A", "030101", "0", "100", 0),
                    kostraRecordInTest("0A", "030101", "0", "710", -1000)
                ), false
            ),
            row(
                "riktig skjema, regnskap og kommune, men driftsregnskap",
                listOf(
                    kostraRecordInTest("0A", "123400", "1", "100", 0),
                    kostraRecordInTest("0A", "123400", "1", "710", -1000)
                ), false
            ),
            row(
                "riktig regnskap og art, men sum overforinger + innsamledeMidler er utenfor interval på -30",
                listOf(
                    kostraRecordInTest("0A", "123400", "0", "100", 0),
                    kostraRecordInTest("0A", "123400", "0", "710", -1000)
                ),
                true
            ),
            row(
                "riktig regnskap og art, men sum overforinger + innsamledeMidler er utenfor interval på +30",
                listOf(
                    kostraRecordInTest("0A", "123400", "0", "100", 1000),
                    kostraRecordInTest("0A", "123400", "0", "710", 0)
                ),
                true
            ),
            row(
                "alt riktig, differanse = 0",
                listOf(
                    kostraRecordInTest("0A", "123400", "0", "100", 1000),
                    kostraRecordInTest("0A", "123400", "0", "710", -1000)
                ), false
            )
        ) { description, kostraRecordList, expectError ->
            val investeringUtgifter = kostraRecordList[0].fieldAsIntOrDefault(RegnskapConstants.FIELD_BELOP)
            val investeringInntekter = kostraRecordList[1].fieldAsIntOrDefault(RegnskapConstants.FIELD_BELOP)
            val investeringDifferanse = investeringUtgifter + investeringInntekter

            When(description) {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér differansen ($investeringDifferanse) mellom inntekter " +
                            "($investeringInntekter) og utgifter ($investeringUtgifter) i investeringsregnskapet"
                )
            }
        }
    }

}) {
    companion object {
        private fun kostraRecordInTest(
            skjema: String,
            region: String,
            kontoklasse : String,
            art: String,
            belop: Int,
        ) = RegnskapTestUtils.regnskapRecordInTest(
            mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_REGION to region,
                RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_BELOP to belop.toString()
            )
        )
    }
}