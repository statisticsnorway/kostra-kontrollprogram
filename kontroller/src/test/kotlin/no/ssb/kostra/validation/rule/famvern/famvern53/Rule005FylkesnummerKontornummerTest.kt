package no.ssb.kostra.validation.rule.famvern.famvern53

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern53.Familievern53ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule005FylkesnummerKontornummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule005FylkesnummerKontornummer(
                listOf(
                    FamilievernConstants.FamvernHierarchyKontorFylkeRegionMapping(kontor = "017", fylke = "31", region = "667200")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid fylke and kontor",
                kostraRecordInTest("31", "017"),
            ),
            ForAllRowItem(
                "valid region and invalid kontor",
                kostraRecordInTest("31", "XXX"),
                expectedErrorMessage = "Fylkesnummer '31' og kontornummer 'XXX' stemmer ikke overens.",
            ),
            ForAllRowItem(
                "invalid region and valid kontor",
                kostraRecordInTest("XX", "017"),
                expectedErrorMessage = "Fylkesnummer 'XX' og kontornummer '017' stemmer ikke overens.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(fylke: String, kontor: String) = listOf(
            Familievern53TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern53ColumnNames.FYLKE_NR_COL_NAME to fylke,
                    Familievern53ColumnNames.KONTORNR_COL_NAME to kontor
                )
            )
        )
    }
}