package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule185Funksjon465DriftTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule185Funksjon465Drift(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "All good",
                listOf(
                    kostraRecordInTest("0C", 1, 465, 0)
                ),
            ),
            ForAllRowItem(
                "skjema not isFylkeskommuneRegnskap",
                listOf(
                    kostraRecordInTest("0A", 1, 465, 100)
                ),
            ),
            ForAllRowItem(
                "kontoklasse is not investering",
                listOf(
                    kostraRecordInTest("0C", 0, 465, 100)
                ),
            ),
            ForAllRowItem(
                "funksjon not 465",
                listOf(
                    kostraRecordInTest("0C", 1, 100, 100)
                ),
            ),
            ForAllRowItem(
                "sum of belop is -30",
                listOf(
                    kostraRecordInTest("0C", 1, 465, -30)
                ),
            ),
            ForAllRowItem(
                "sum of belop is 30",
                listOf(
                    kostraRecordInTest("0C", 1, 465, 30)
                ),
            ),
            ForAllRowItem(
                "sum of belop is -31",
                listOf(
                    kostraRecordInTest("0C", 1, 465, -31)
                ),
                expectedErrorMessage = "Korrigér i fila slik at differanse (-31) på funksjon 465 " +
                        "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                        "(margin på +/- 30')"
            ),
            ForAllRowItem(
                "sum of belop is 31",
                listOf(
                    kostraRecordInTest("0C", 1, 465, 31)
                ),
                expectedErrorMessage = "Korrigér i fila slik at differanse (31) på funksjon 465 " +
                        "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                        "(margin på +/- 30')"
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            skjema: String,
            kontoklasse: Int,
            funksjon: Int,
            belop: Int
        ) = mapOf(
            RegnskapConstants.FIELD_REGION to "420000",
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_KONTOKLASSE to "$kontoklasse",
            RegnskapConstants.FIELD_FUNKSJON to "$funksjon",
            RegnskapConstants.FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
    }
}