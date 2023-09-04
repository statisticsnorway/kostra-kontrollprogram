package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule014KontrollAvTotalsummerForMeklingerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule014KontrollAvTotalsummerForMeklinger(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "all identical values",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "values vary",
                kostraRecordInTest("9"),
                expectedErrorMessage = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: [(MEKLING_TOT_ALLE, 9), (ENBEGGE_TOT, 1), (VENTETID_TOT_TOT, 1), (VARIGHET_TOT_TOT, 1), (RESULT_TOT_TOT, 1), (BEKYMR_TOT_TOT, 1)]",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(sum: String) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.MEKLING_TOT_ALLE_COL_NAME to sum,
                    Familievern55ColumnNames.ENBEGGE_TOT_COL_NAME to "1",
                    Familievern55ColumnNames.VENTETID_TOT_TOT_COL_NAME to "1",
                    Familievern55ColumnNames.VARIGHET_TOT_TOT_COL_NAME to "1",
                    Familievern55ColumnNames.RESULT_TOT_TOT_COL_NAME to "1",
                    Familievern55ColumnNames.BEKYMR_TOT_TOT_COL_NAME to "1"
                )
            )
        )
    }
}