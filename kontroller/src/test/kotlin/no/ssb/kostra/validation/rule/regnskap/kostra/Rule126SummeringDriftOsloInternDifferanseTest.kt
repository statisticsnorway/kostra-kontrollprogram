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
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule126SummeringDriftOsloInternDifferanseTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule126SummeringDriftOsloInternDifferanse(),
            forAllRows = listOf(
                ForAllRowItem(
                    "All good",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 298, 1),
                        kostraRecordInTest("030100", "0A", 1, 798, -1)
                    ),
                ),

                ForAllRowItem(
                    "region is not Oslo, isOsloInternRegnskap is false",
                    listOf(
                        kostraRecordInTest("XXXXXX", "0A", 1, 298, 1),
                        kostraRecordInTest("XXXXXX", "0A", 1, 798, -1)
                    ),
                ),
                ForAllRowItem(
                    "skjema not in 0A or 0M, isOsloInternRegnskap is false",
                    listOf(
                        kostraRecordInTest("030100", "XX", 1, 298, 1),
                        kostraRecordInTest("030100", "XX", 1, 798, -1)
                    ),
                ),
                ForAllRowItem(
                    "kontoklasse is not drift",
                    listOf(
                        kostraRecordInTest("030100", "0A", 0, 298, 1),
                        kostraRecordInTest("030100", "0A", 0, 798, -1)
                    ),
                ),
                ForAllRowItem(
                    "art not in 298 or 798",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 100, 1),
                        kostraRecordInTest("030100", "0A", 1, 100, -1)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -10",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 298, 1),
                        kostraRecordInTest("030100", "0A", 1, 798, -11)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is 10",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 298, 11),
                        kostraRecordInTest("030100", "0A", 1, 798, -1)
                    ),
                ),
                ForAllRowItem(
                    "sum of belop is -11",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 298, 1),
                        kostraRecordInTest("030100", "0A", 1, 798, -12)
                    ),
                    expectedErrorMessage = "Korrigér differansen (-11) mellom sum over alle funksjoner for art 298 " +
                            "(1) og sum over alle funksjoner for art 798 (-12) i driftsregnskapet."
                ),
                ForAllRowItem(
                    "sum of belop is 11",
                    listOf(
                        kostraRecordInTest("030100", "0A", 1, 298, 12),
                        kostraRecordInTest("030100", "0A", 1, 798, -1)
                    ),
                    expectedErrorMessage = "Korrigér differansen (11) mellom sum over alle funksjoner for art 298 " +
                            "(12) og sum over alle funksjoner for art 798 (-1) i driftsregnskapet."
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