package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule005RegionsnummerKontornummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule005RegionsnummerKontornummer(
                listOf(
                    FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping(kontor = "017", fylke = "31", region = "667200")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid region and kontor",
                kostraRecordInTest("667200", "017"),
            ),
            ForAllRowItem(
                "valid region and invalid kontor",
                kostraRecordInTest("667200", "XXX"),
                expectedErrorMessage = "Regionsnummer '667200' og kontornummer 'XXX' stemmer ikke overens.",
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
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52bColumnNames.REGION_NR_B_COL_NAME to region,
                    Familievern52bColumnNames.KONTOR_NR_B_COL_NAME to kontor
                )
            )
        )
    }
}