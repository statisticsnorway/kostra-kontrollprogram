package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule175Funksjon290DriftTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule175Funksjon290Drift(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "All good",
                listOf(
                    kostraRecordInTest("420400", "0A", 1, 290, 0)
                ),
            ),

            ForAllRowItem(
                "region is Oslo Bydel 01",
                listOf(
                    kostraRecordInTest("030101", "0A", 1, 290, 100)
                ),
            ),
            ForAllRowItem(
                "skjema not isKommuneRegnskap",
                listOf(
                    kostraRecordInTest("420400", "0C", 1, 290, 100)
                ),
            ),
            ForAllRowItem(
                "kontoklasse is not drift",
                listOf(
                    kostraRecordInTest("420400", "0A", 0, 290, 100)
                ),
            ),
            ForAllRowItem(
                "funksjon not 290",
                listOf(
                    kostraRecordInTest("420400", "0A", 1, 100, 100)
                ),
            ),
            ForAllRowItem(
                "sum of belop is -30",
                listOf(
                    kostraRecordInTest("420400", "0A", 1, 290, -30)
                ),
            ),
            ForAllRowItem(
                "sum of belop is 30",
                listOf(
                    kostraRecordInTest("420400", "0A", 1, 290, 30)
                ),
            ),
            ForAllRowItem(
                "sum of belop is -31",
                listOf(
                    kostraRecordInTest("420400", "0A", 1, 290, -31)
                ),
                expectedErrorMessage = "Korrigér i fila slik at differanse (-31) på funksjon 290 " +
                        "interkommunale samarbeid går i 0 i driftsregnskapet. (margin på +/- 30')"
            ),
            ForAllRowItem(
                "sum of belop is 31",
                listOf(
                    kostraRecordInTest("420400", "0A", 1, 290, 31)
                ),
                expectedErrorMessage = "Korrigér i fila slik at differanse (31) på funksjon 290 " +
                        "interkommunale samarbeid går i 0 i driftsregnskapet. (margin på +/- 30')"
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            region: String,
            skjema: String,
            kontoklasse: Int,
            funksjon: Int,
            belop: Int
        ) = mapOf(
            RegnskapConstants.FIELD_REGION to region,
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_KONTOKLASSE to "$kontoklasse",
            RegnskapConstants.FIELD_FUNKSJON to "$funksjon",
            RegnskapConstants.FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
    }
}