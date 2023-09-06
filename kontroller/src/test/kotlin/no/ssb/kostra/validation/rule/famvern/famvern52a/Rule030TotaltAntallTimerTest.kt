package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule030TotaltAntallTimerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule030TotaltAntallTimer(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre " +
                        "ved kontoret har anvendt på saken (timer benyttet til gruppesamtaler skal holdes utenfor) " +
                        "i løpet av året (for og etterarbeid skal ikke regnes med). " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange timer hovedterapeut eller andre " +
                        "ved kontoret har anvendt på saken (timer benyttet til gruppesamtaler skal holdes utenfor) " +
                        "i løpet av året (for og etterarbeid skal ikke regnes med). " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(count: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.TIMER_IARET_A_COL_NAME to count)
            )
        )
    }
}