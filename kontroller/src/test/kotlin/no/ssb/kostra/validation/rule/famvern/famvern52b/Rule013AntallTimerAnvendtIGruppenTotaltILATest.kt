package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule013AntallTimerAnvendtIGruppenTotaltILATest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule013AntallTimerAnvendtIGruppenTotaltILA(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen " +
                        "i løpet av året. (For og etterarbeid skal ikke regnes med).",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange timer som er anvendt for gruppen " +
                        "i løpet av året. (For og etterarbeid skal ikke regnes med).",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(count: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.TIMERTOT_IARET_B_COL_NAME to count)
            )
        )
    }
}