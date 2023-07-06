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
                    "only eiendeler record",
                    listOf(
                        kostraRecordInTest("0B", 10, 11),
                    ),
                    expectedErrorMessage = "Korrigér differansen (11) mellom eiendeler (11) og gjeld " +
                            "og egenkapital (0) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "only gjeld record",
                    listOf(
                        kostraRecordInTest("0B", 31, -11)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-11) mellom eiendeler (0) og gjeld " +
                            "og egenkapital (-11) i fila (Differanser opptil ±10' godtas)"
                ),

                ForAllRowItem(
                    "isBalanseRegnskap = true, aktiva + passiva negative",
                    listOf(
                        kostraRecordInTest("0B", 10, 10),
                        kostraRecordInTest("0B", 31, -21)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-11) mellom eiendeler (10) og gjeld " +
                            "og egenkapital (-21) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = true, aktiva + passiva positive",
                    listOf(
                        kostraRecordInTest("0B", 10, 21),
                        kostraRecordInTest("0B", 31, -10)
                    ),
                    expectedErrorMessage = "Korrigér differansen (11) mellom eiendeler (21) og gjeld " +
                            "og egenkapital (-10) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "negative aktiva",
                    listOf(
                        kostraRecordInTest("0B", 10, -1),
                        kostraRecordInTest("0B", 31, -1)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-2) mellom eiendeler (-1) og gjeld " +
                            "og egenkapital (-1) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "positive gjeld",
                    listOf(
                        kostraRecordInTest("0B", 10, 1),
                        kostraRecordInTest("0B", 31, 2)
                    ),
                    expectedErrorMessage = "Korrigér differansen (3) mellom eiendeler (1) og gjeld " +
                            "og egenkapital (2) i fila (Differanser opptil ±10' godtas)"
                ),
                ForAllRowItem(
                    "isBalanseRegnskap = false",
                    listOf(
                        kostraRecordInTest("0A", 10, 1),
                        kostraRecordInTest("0A", 31, -1)
                    )
                ),
                ForAllRowItem(
                    "amount within limit, lower limit",
                    listOf(
                        kostraRecordInTest("0A", 10, 1),
                        kostraRecordInTest("0A", 31, -11)
                    )
                ),
                ForAllRowItem(
                    "amount within limit, upper limit",
                    listOf(
                        kostraRecordInTest("0A", 10, 11),
                        kostraRecordInTest("0A", 31, -1)
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