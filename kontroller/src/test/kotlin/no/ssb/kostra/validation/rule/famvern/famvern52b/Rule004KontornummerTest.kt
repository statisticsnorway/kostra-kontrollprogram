package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule004KontornummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule004Kontornummer(
                listOf(
                    FamilievernConstants.KontorFylkeRegionMapping("017", "30", "667600")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid kontor",
                kostraRecordInTest("017"),
            ),
            ForAllRowItem(
                "invalid kontor",
                kostraRecordInTest("XXX"),
                expectedErrorMessage = "Kontornummeret som er oppgitt i recorden fins ikke i listen med " +
                        "gyldige kontornumre. Fant 'XXX', forventet én av : [017].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(region: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.KONTOR_NR_B_COL_NAME to region)
            )
        )
    }
}