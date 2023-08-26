package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule009AvsluttedeMeklingerEtterVarighetTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule009AvsluttedeMeklingerEtterVarighet(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1", "1", "1")
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("2", "3", "4"),
                expectedErrorMessage = "Summen (VARIGHET_SEP_TOT) med verdi (2) er ulik summen (1) av følgende liste ([(VARIGHET_SEP_1, 1), (VARIGHET_SEP_2, 0), (VARIGHET_SEP_3, 0)])",
                expectedSize = 4
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            rowSum: String,
            columnSum: String,
            sumOfSums: String
        ) = listOf(Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.VARIGHET_SEP_1_COL_NAME to "1",
                    Familievern55ColumnNames.VARIGHET_SEP_TOT_COL_NAME to rowSum,
                    Familievern55ColumnNames.VARIGHET_TOT_1_COL_NAME to columnSum,
                    Familievern55ColumnNames.VARIGHET_TOT_TOT_COL_NAME to sumOfSums
                )
            )
        )
    }
}