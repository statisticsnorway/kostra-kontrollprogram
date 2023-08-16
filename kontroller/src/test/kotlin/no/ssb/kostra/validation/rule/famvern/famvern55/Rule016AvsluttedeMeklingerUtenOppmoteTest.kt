package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule016AvsluttedeMeklingerUtenOppmoteTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule016AvsluttedeMeklingerUtenOppmote(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1", "1", "2"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("1", "1", "3"),
                expectedErrorMessage = "Summen (UTEN_OPPM_TOT) med verdi (3) er ulik summen (2) av følgende liste ([(UTEN_OPPM_1, 1), (UTEN_OPPM_2, 1), (UTEN_OPPM_3, 0), (UTEN_OPPM_4, 0), (UTEN_OPPM_5, 0)])",
                expectedSize = 1
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            number1: String,
            number2: String,
            rowSum: String
        ) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.UTEN_OPPM_1_COL_NAME to number1,
                    Familievern55ColumnNames.UTEN_OPPM_2_COL_NAME to number2,
                    Familievern55ColumnNames.UTEN_OPPM_TOT_COL_NAME to rowSum
                )
            )
        )
    }
}