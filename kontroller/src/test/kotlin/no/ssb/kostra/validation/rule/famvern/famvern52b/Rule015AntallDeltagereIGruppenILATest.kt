package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule015AntallDeltagereIGruppenILATest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule015AntallDeltagereIGruppenILA(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid count",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing count",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange som har deltatt i gruppen " +
                        "i løpet av året. Terapeuter holdes utenom.",
            ),
            ForAllRowItem(
                "invalid count, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hvor mange som har deltatt i gruppen " +
                        "i løpet av året. Terapeuter holdes utenom.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(count: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.ANTDELT_IARET_B_COL_NAME to count)
            )
        )
    }
}