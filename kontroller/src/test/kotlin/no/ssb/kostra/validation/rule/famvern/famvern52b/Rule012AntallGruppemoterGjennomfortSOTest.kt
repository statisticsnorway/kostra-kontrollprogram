package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule012AntallGruppemoterGjennomfortSOTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule012AntallGruppemoterGjennomfortSO(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt " +
                        "siden gruppen ble opprettet.",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange gruppemøter det er gjennomført i alt " +
                        "siden gruppen ble opprettet.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(count: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.ANTMOTERTOT_OPPR_B_COL_NAME to count)
            )
        )
    }
}