package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule003RegionsnummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule003Regionsnummer(
                listOf(
                    FamilievernConstants.KontorFylkeRegionMapping("017", "30", "667600")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid region",
                kostraRecordInTest("667600"),
            ),
            ForAllRowItem(
                "invalid region",
                kostraRecordInTest("XXXXXX"),
                expectedErrorMessage = "Regionsnummeret som er oppgitt i recorden fins ikke i listen med " +
                        "gyldige regionsnumre. Fant 'XXXXXX', forventet én av : [667600].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(region: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.REGION_NR_B_COL_NAME to region)
            )
        )
    }
}