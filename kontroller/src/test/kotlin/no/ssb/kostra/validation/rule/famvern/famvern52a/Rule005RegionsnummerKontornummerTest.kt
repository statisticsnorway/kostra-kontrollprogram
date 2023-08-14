package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aMain
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule005RegionsnummerKontornummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule005RegionsnummerKontornummer(
                listOf(
                    Familievern52aMain.KontorFylkeRegionMapping("017", "30", "667600")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid region and kontor",
                kostraRecordInTest("667600", "017"),
            ),
            ForAllRowItem(
                "valid region and invalid kontor",
                kostraRecordInTest("667600", "XXX"),
                expectedErrorMessage = "Regionsnummer '667600' og kontornummer 'XXX' stemmer ikke overens.",
            ),
            ForAllRowItem(
                "invalid region and valid kontor",
                kostraRecordInTest("XXXXXX", "017"),
                expectedErrorMessage = "Regionsnummer 'XXXXXX' og kontornummer '017' stemmer ikke overens.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(region: String, kontor: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.REGION_NR_A_COL_NAME to region,
                    Familievern52aColumnNames.KONTOR_NR_A_COL_NAME to kontor
                )
            )
        )
    }
}