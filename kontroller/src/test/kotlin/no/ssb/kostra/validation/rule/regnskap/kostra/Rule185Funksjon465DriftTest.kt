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
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule185Funksjon465DriftTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule185Funksjon465Drift(),
            forAllRows = listOf(
                ForAllRowItem(
                    "All good",
                    listOf(
                        kostraRecordInTest("420000", "0C", 1, 465, 0)
                    ),
                ),

                ForAllRowItem(
                    "skjema not isFylkeskommuneRegnskap",
                    listOf(
                        kostraRecordInTest("420000", "0A", 1, 465, 100)
                    ),
                ),
                ForAllRowItem(
                    "kontoklasse is not investering",
                    listOf(
                        kostraRecordInTest("420000", "0C", 0, 465, 100)
                    ),
                ),
                ForAllRowItem(
                    "funksjon not 465",
                    listOf(
                        kostraRecordInTest("420000", "0C", 1, 100, 100)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -30",
                    listOf(
                        kostraRecordInTest("420000", "0C", 1, 465, -30)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is 30",
                    listOf(
                        kostraRecordInTest("420000", "0C", 1, 465, 30)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -31",
                    listOf(
                        kostraRecordInTest("420000", "0C", 1, 465, -31)
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differanse (-31) på funksjon 465 " +
                            "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                            "(margin på +/- 30')"
                ),
                ForAllRowItem(
                    "sum of belop is 31",
                    listOf(
                        kostraRecordInTest("420000", "0C", 1, 465, 31)
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differanse (31) på funksjon 465 " +
                            "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                            "(margin på +/- 30')"
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