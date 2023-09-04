package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule014AntallTimerAnvendtIGruppenTotaltSOTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule014AntallTimerAnvendtIGruppenTotaltSO(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen " +
                        "siden opprettelsen",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen " +
                        "siden opprettelsen",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(count: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.TIMERTOT_OPPR_B_COL_NAME to count)
            )
        )
    }
}