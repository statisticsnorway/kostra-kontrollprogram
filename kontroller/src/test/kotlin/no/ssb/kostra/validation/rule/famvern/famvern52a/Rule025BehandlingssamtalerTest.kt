package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule025BehandlingssamtalerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule025Behandlingssamtaler(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut ut hvor mange behandlingssamtaler de ulike deltakerne i saken har " +
                        "deltatt i gjennom året. Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område.",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut ut hvor mange behandlingssamtaler de ulike deltakerne i saken har " +
                        "deltatt i gjennom året. Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(count: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.SAMT_PRIMK_A_COL_NAME to count)
            )
        )
    }
}