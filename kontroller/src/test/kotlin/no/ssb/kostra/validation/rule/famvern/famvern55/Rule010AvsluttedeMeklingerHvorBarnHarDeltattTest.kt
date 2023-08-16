package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule010AvsluttedeMeklingerHvorBarnHarDeltattTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule010AvsluttedeMeklingerHvorBarnHarDeltatt(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1", "1", "2"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("1", "1", "3"),
                expectedErrorMessage = "Summen (BARNDELT_TOT_TOT) med verdi (3) er ulik summen (2) av følgende liste ([(BARNDELT_SEP_TOT, 0), (BARNDELT_SAM_TOT, 0), (BARNDELT_SAK_TOT, 0), (BARNDELT_TILB_TOT, 1), (BARNDELT_FLY_TOT, 1)])",
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
                    Familievern55ColumnNames.BARNDELT_TILB_TOT_COL_NAME to number1,
                    Familievern55ColumnNames.BARNDELT_FLY_TOT_COL_NAME to number2,
                    Familievern55ColumnNames.BARNDELT_TOT_TOT_COL_NAME to rowSum,
                )
            )
        )
    }
}