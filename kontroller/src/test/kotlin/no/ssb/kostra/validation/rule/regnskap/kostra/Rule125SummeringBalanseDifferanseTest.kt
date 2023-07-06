package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule125SummeringBalanseDifferanseTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule125SummeringBalanseDifferanse(),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBalanseRegnskap = true",
                    listOf(
                        kostraRecordInTest("0B", 10, 10),
                        kostraRecordInTest("0B", 31, -21)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-11) mellom eiendeler (10) og gjeld " +
                            "og egenkapital (-21) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = true, negative aktiva",
                    listOf(
                        kostraRecordInTest("0B", 10, -10),
                        kostraRecordInTest("0B", 31, -21)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-31) mellom eiendeler (-10) og gjeld " +
                            "og egenkapital (-21) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = true, positive gjeld",
                    listOf(
                        kostraRecordInTest("0B", 10, 10),
                        kostraRecordInTest("0B", 31, 21)
                    ),
                    expectedErrorMessage = "Korrigér differansen (31) mellom eiendeler (10) og gjeld " +
                            "og egenkapital (21) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = false",
                    listOf(
                        kostraRecordInTest("0A", 10, 10),
                        kostraRecordInTest("0A", 31, -11)
                    )
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            skjema: String,
            kapittel: Int,
            belop: Int
        ) = mapOf(
            FIELD_SKJEMA to skjema,
            FIELD_KONTOKLASSE to "2",
            FIELD_KAPITTEL to "$kapittel",
            FIELD_BELOP to "$belop"
        ).toKostraRecord()
    }
}