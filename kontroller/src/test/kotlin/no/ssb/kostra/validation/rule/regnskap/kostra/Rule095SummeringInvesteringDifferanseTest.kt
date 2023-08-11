package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule095SummeringInvesteringDifferanseTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule095SummeringInvesteringDifferanse(),
            forAllRows = listOf(
                ForAllRowItem(
                    "All good",
                    listOf(
                        kostraRecordInTest("030100", "0A", 0, 100, 100),
                        kostraRecordInTest("030100", "0A", 0, 710, -100)
                    ),
                ),

                ForAllRowItem(
                    "region is Oslo bydel 01",
                    listOf(
                        kostraRecordInTest("030101", "0A", 0, 100, 1000),
                        kostraRecordInTest("030101", "0A", 0, 710, -100)
                    ),
                ),
                ForAllRowItem(
                    "skjema is not BevilgningRegnskap",
                    listOf(
                        kostraRecordInTest("030100", "XX", 0, 100, 1000),
                        kostraRecordInTest("030100", "xx", 0, 710, -100)
                    ),
                ),
                ForAllRowItem(
                    "kontoklasse is not Investering",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 100, 1000),
                        kostraRecordInTest("030100", "0A", 1, 710, -100)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -10",
                    listOf(
                        kostraRecordInTest("030100", "0A", 0, 100, 100),
                        kostraRecordInTest("030100", "0A", 0, 710, -110)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is 10",
                    listOf(
                        kostraRecordInTest("030100", "0A", 0, 100, 110),
                        kostraRecordInTest("030100", "0A", 0, 710, -100)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -31",
                    listOf(
                        kostraRecordInTest("030100", "0A", 0, 100, 100),
                        kostraRecordInTest("030100", "0A", 0, 710, -131)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-31) mellom inntekter " +
                            "(-131) og utgifter (100) i investeringsregnskapet"
                ),
                ForAllRowItem(
                    "sum of belop is 31",
                    listOf(
                        kostraRecordInTest("030100", "0A", 0, 100, 131),
                        kostraRecordInTest("030100", "0A", 0, 710, -100)
                    ),
                    expectedErrorMessage = "Korrigér differansen (31) mellom inntekter " +
                            "(-100) og utgifter (131) i investeringsregnskapet"
                ),
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            region: String,
            skjema: String,
            kontoklasse: Int,
            art: Int,
            belop: Int
        ) = mapOf(
            RegnskapConstants.FIELD_REGION to region,
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_KONTOKLASSE to "$kontoklasse",
            RegnskapConstants.FIELD_FUNKSJON to "100",
            RegnskapConstants.FIELD_ART to "$art",
            RegnskapConstants.FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
    }
}