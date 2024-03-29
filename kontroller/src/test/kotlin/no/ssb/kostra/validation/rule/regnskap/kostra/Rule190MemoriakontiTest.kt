package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest


class Rule190MemoriakontiTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule190Memoriakonti(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "isBalanseRegnskap = true, kapittel match, kapittel #2 match, sum belop match #1",
                listOf(
                    kostraRecordInTest("0B", "9100", "000", "100"),
                    kostraRecordInTest("0B", "9999", "990", "-131")
                ),
                expectedErrorMessage = "Korrigér i fila slik at differansen (-31) mellom memoriakontiene (100) " +
                        "og motkonto for memoriakontiene (-131) går i 0. (margin på +/- 10')",
            ),
            ForAllRowItem(
                "isBalanseRegnskap = true, kapittel match, kapittel #2 match, sum belop match #2",
                listOf(
                    kostraRecordInTest("0B", "9100", "000", "-100"),
                    kostraRecordInTest("0B", "9999", "990", "131")
                ),
                expectedErrorMessage = "Korrigér i fila slik at differansen (31) mellom memoriakontiene (-100) " +
                        "og motkonto for memoriakontiene (131) går i 0. (margin på +/- 10')",
            ),
            ForAllRowItem(
                "isBalanseRegnskap = false, kapittel match, kapittel #2 match, sum belop match",
                listOf(
                    kostraRecordInTest("0A", "9100", "000", "100"),
                    kostraRecordInTest("0A", "9999", "990", "-1000")
                ),
            ),
            ForAllRowItem(
                "isBalanseRegnskap = true, kapittel mismatch #1, kapittel #2 match, sum belop mismatch",
                listOf(
                    kostraRecordInTest("0B", "9099", "000", "100"),
                    kostraRecordInTest("0B", "10000", "990", "-10")
                ),
            ),
            ForAllRowItem(
                "isBalanseRegnskap = true, kapittel mismatch #2, kapittel #2 match, sum belop mismatch",
                listOf(
                    kostraRecordInTest("0B", "10000", "000", "100"),
                    kostraRecordInTest("0B", "9099", "990", "-10")
                ),
            ),
            ForAllRowItem(
                "isBalanseRegnskap = true, kapittel match, kapittel #2 match, sum belop mismatch #1",
                listOf(
                    kostraRecordInTest("0B", "9100", "000", "130"),
                    kostraRecordInTest("0B", "9999", "990", "-100")
                ),
            ),
            ForAllRowItem(
                "isBalanseRegnskap = true, kapittel match, kapittel #2 match, sum belop mismatch #2",
                listOf(
                    kostraRecordInTest("0B", "9100", "000", "-130"),
                    kostraRecordInTest("0B", "9999", "990", "100")
                ),
            )
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
        ).toKostraRecord(1, fieldDefinitions)
    }
}
