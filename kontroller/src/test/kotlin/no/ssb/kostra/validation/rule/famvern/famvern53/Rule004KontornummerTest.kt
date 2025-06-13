package no.ssb.kostra.validation.rule.famvern.famvern53

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule004KontornummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule004Kontornummer(
                listOf(
                    FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping(kontor = "017", fylke = "31", region = "667200")
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
            Familievern53TestUtils.familievernRecordInTest(
                mapOf(Familievern53ColumnNames.KONTORNR_COL_NAME to region)
            )
        )
    }
}