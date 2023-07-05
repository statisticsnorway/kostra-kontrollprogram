package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord


class Rule190MemoriakontiTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule190Memoriakonti(),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBalanseRegnskap = true, kapittel matching, kapittel #2 matching, sum belop matching",
                    listOf(
                        kostraRecordInTest("0B", "9100", "000", "100"),
                        kostraRecordInTest("0B", "9999", "990", "-1000")
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differansen (-900) mellom memoriakontiene (100) " +
                            "og motkonto for memoriakontiene (-1000) går i 0. (margin på +/- 10')"
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = false, kapittel matching, kapittel #2 matching, sum belop matching",
                    listOf(
                        kostraRecordInTest("0A", "9100", "000", "100"),
                        kostraRecordInTest("0A", "9999", "990", "-1000")
                    )
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = true, kapittel not matching, kapittel #2 matching, sum belop not matching",
                    listOf(
                        kostraRecordInTest("0B", "9099", "000", "100"),
                        kostraRecordInTest("0B", "9999", "990", "-10")
                    )
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = true, kapittel matching, kapittel #2 matching, sum belop not matching",
                    listOf(
                        kostraRecordInTest("0B", "9100", "000", "100"),
                        kostraRecordInTest("0B", "9999", "990", "-100")
                    )
                ),
            ),
            expectedSeverity = Severity.WARNING,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            skjema: String,
            kapittel: String,
            sektor: String,
            belop: String
        ) = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_KONTOKLASSE to "2",
            FIELD_KAPITTEL to kapittel,
            FIELD_SEKTOR to sektor,
            FIELD_BELOP to belop
        ).toKostraRecord()
    }
}
