package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aMain
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule004KontornummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule004Kontornummer(
                listOf(
                    Familievern52aMain.KontorFylkeRegionMapping("017", "30", "667600")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid region",
                kostraRecordInTest("017"),
            ),
            ForAllRowItem(
                "invalid region",
                kostraRecordInTest("XXX"),
                expectedErrorMessage = "Kontornummeret som er oppgitt i recorden fins ikke i listen med " +
                        "gyldige kontornumre. Fant 'XXX', forventet én av : [017].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(region: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.KONTOR_NR_A_COL_NAME to region)
            )
        )
    }
}