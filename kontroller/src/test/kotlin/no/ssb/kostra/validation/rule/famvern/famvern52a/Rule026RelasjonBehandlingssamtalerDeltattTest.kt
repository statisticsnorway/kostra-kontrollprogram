package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule026RelasjonBehandlingssamtalerDeltattTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule026RelasjonBehandlingssamtalerDeltatt(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "no participation",
                kostraRecordInTest(" ", " "),
            ),
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1", "1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest("1", " "),
                expectedErrorMessage = "Det er oppgitt at andre personer (Partner) har deltatt i samtaler med " +
                        "primærklient i løpet av året, men det er ikke oppgitt hvor mange behandlingssamtaler ( ) " +
                        "de ulike personene har deltatt i gjennom av året.",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("1", "X"),
                expectedErrorMessage = "Det er oppgitt at andre personer (Partner) har deltatt i samtaler med " +
                        "primærklient i løpet av året, men det er ikke oppgitt hvor mange behandlingssamtaler (X) " +
                        "de ulike personene har deltatt i gjennom av året.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(participation: String, count: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.DELT_PARTNER_A_COL_NAME to participation,
                    Familievern52aColumnNames.SAMT_PARTNER_A_COL_NAME to count
                )
            )
        )
    }
}