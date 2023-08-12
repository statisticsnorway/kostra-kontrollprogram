package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule126SummeringDriftOsloInternDifferanseTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoArgsTest(
            sut = Rule126SummeringDriftOsloInternDifferanse(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "All good",
                listOf(
                    kostraRecordInTest("030100", "0A", 1, 298, 100),
                    kostraRecordInTest("030100", "0A", 1, 798, -100)
                ),
            ),
            ForAllRowItem(
                "region is not Oslo, isOsloInternRegnskap is false",
                listOf(
                    kostraRecordInTest("XXXXXX", "0A", 1, 298, 100),
                    kostraRecordInTest("XXXXXX", "0A", 1, 798, -100)
                ),
            ),
            ForAllRowItem(
                "skjema not in 0A or 0M, isOsloInternRegnskap is false",
                listOf(
                    kostraRecordInTest("030100", "XX", 1, 298, 100),
                    kostraRecordInTest("030100", "XX", 1, 798, -100)
                ),
            ),
            ForAllRowItem(
                "kontoklasse is not drift",
                listOf(
                    kostraRecordInTest("030100", "0A", 0, 298, 100),
                    kostraRecordInTest("030100", "0A", 0, 798, -100)
                ),
            ),
            ForAllRowItem(
                "art not in 298 or 798",
                listOf(
                    kostraRecordInTest("030100", "0A", 1, 100, 100),
                    kostraRecordInTest("030100", "0A", 1, 100, -100)
                ),
            ),
            ForAllRowItem(
                "sum of belop is -10",
                listOf(
                    kostraRecordInTest("030100", "0A", 1, 298, 100),
                    kostraRecordInTest("030100", "0A", 1, 798, -110)
                ),
            ),
            ForAllRowItem(
                "sum of belop is 10",
                listOf(
                    kostraRecordInTest("030100", "0A", 1, 298, 110),
                    kostraRecordInTest("030100", "0A", 1, 798, -100)
                ),
            ),
            ForAllRowItem(
                "sum of belop is -11",
                listOf(
                    kostraRecordInTest("030100", "0A", 1, 298, 100),
                    kostraRecordInTest("030100", "0A", 1, 798, -111)
                ),
                expectedErrorMessage = "Korrigér differansen (-11) mellom sum over alle funksjoner for art " +
                        "298 (100) og sum over alle funksjoner for art 798 (-111) i driftsregnskapet."
            ),
            ForAllRowItem(
                "sum of belop is 11",
                listOf(
                    kostraRecordInTest("030100", "0A", 1, 298, 111),
                    kostraRecordInTest("030100", "0A", 1, 798, -100)
                ),
                expectedErrorMessage = "Korrigér differansen (11) mellom sum over alle funksjoner for art " +
                        "298 (111) og sum over alle funksjoner for art 798 (-100) i driftsregnskapet."
            )
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