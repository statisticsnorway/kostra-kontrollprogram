package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.FamilievernConstants
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule003FylkesnummerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule003Fylkesnummer(
                listOf(
                    FamilievernConstants.KontorFylkeRegionMapping("017", "30", "667600")
                )
            ),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid fylke",
                kostraRecordInTest("30"),
            ),
            ForAllRowItem(
                "invalid fylke",
                kostraRecordInTest("XX"),
                expectedErrorMessage = "Fylkesnummeret som er oppgitt i recorden fins ikke i listen med " +
                        "gyldige fylkesnumre. Fant 'XX', forventet én av : [30].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(region: String) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(Familievern55ColumnNames.FYLKE_NR_COL_NAME to region)
            )
        )
    }
}