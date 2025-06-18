package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule017BarnDeltattTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule017BarnDeltatt(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("4"),
                expectedErrorMessage = "Summen (BARN_TOT) med verdi (4) er ulik summen (1) av følgende liste ([(BARN_DELT, 1), (BARN_IKKE_DELT, 0)])",
                expectedSize = 1
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            sumOfSums: String
        ) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.BARN_DELT_COL_NAME to "1",
                    Familievern55ColumnNames.BARN_TOT_COL_NAME to sumOfSums,
                )
            )
        )
    }
}