package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule032SamarbeidMedAndreInstanserSidenOpprettelsenTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule032SamarbeidMedAndreInstanserSidenOpprettelsen(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "No cooperation is checked, other is unchecked",
                kostraRecordInTest("1", " "),
            ),
            ForAllRowItem(
                "No cooperation is checked, but other is also checked",
                kostraRecordInTest("1", "1"),
                expectedErrorMessage = "Det er ikke krysset av for om det har vært samarbeid med andre " +
                        "instanser siden saken ble opprettet. Feltet er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "No cooperation is unchecked, other is checked",
                kostraRecordInTest(" ", "1"),
            ),
            ForAllRowItem(
                "No cooperation is unchecked, but other is also unchecked",
                kostraRecordInTest(" ", " "),
                expectedErrorMessage = "Det er ikke krysset av for om det har vært samarbeid med andre " +
                        "instanser siden saken ble opprettet. Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(cooperation: String, other: String) =
            listOf(
                Familievern52aTestUtils.familievernRecordInTest(
                    mapOf(
                        Familievern52aColumnNames.SAMARB_INGEN_A_COL_NAME to cooperation,
                        Familievern52aColumnNames.SAMARB_LEGE_A_COL_NAME to other
                    )
                )
            )
    }
}