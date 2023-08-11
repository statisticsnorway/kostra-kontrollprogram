package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule140OverforingerDriftInvesteringTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule140OverforingerDriftInvestering(),
            forAllRows = listOf(
                ForAllRowItem(
                    "All good",
                    listOf(
                        kostraRecordInTest("420400", "0A", 1, 570, 100),
                        kostraRecordInTest("420400", "0A", 0, 970, -100)
                    ),
                ),

                ForAllRowItem(
                    "region is Oslo Bydel 01",
                    listOf(
                        kostraRecordInTest("030101", "0A", 1, 570, 100),
                        kostraRecordInTest("030101", "0A", 0, 970, -100)
                    ),
                ),
                ForAllRowItem(
                    "skjema not a isBevilgningRegnskap",
                    listOf(
                        kostraRecordInTest("420400", "XX", 1, 570, 100),
                        kostraRecordInTest("420400", "XX", 0, 970, -100)
                    ),
                ),
                ForAllRowItem(
                    "art not in 1.570 or 0.970",
                    listOf(
                        kostraRecordInTest("420400", "0A", 1, 100, 100),
                        kostraRecordInTest("420400", "0A", 0, 100, -100)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -30",
                    listOf(
                        kostraRecordInTest("420400", "0A", 1, 570, 130),
                        kostraRecordInTest("420400", "0A", 0, 970, -100)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is 30",
                    listOf(
                        kostraRecordInTest("420400", "0A", 1, 570, 130),
                        kostraRecordInTest("420400", "0A", 0, 970, -100)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -31",
                    listOf(
                        kostraRecordInTest("420400", "0A", 1, 570, 100),
                        kostraRecordInTest("420400", "0A", 0, 970, -131)
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differansen (-31) i overføringer mellom " +
                            "drifts- (100) og investeringsregnskapet (-131) stemmer overens."
                ),
                ForAllRowItem(
                    "sum of belop is 31",
                    listOf(
                        kostraRecordInTest("420400", "0A", 1, 570, 131),
                        kostraRecordInTest("420400", "0A", 0, 970, -100)
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differansen (31) i overføringer mellom " +
                            "drifts- (131) og investeringsregnskapet (-100) stemmer overens."
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