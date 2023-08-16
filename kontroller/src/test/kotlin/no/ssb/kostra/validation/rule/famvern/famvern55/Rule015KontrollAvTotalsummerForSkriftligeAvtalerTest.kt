package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule015KontrollAvTotalsummerForSkriftligeAvtalerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule015KontrollAvTotalsummerForSkriftligeAvtaler(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "all identical values",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "values vary",
                kostraRecordInTest("9"),
                expectedErrorMessage = "Én eller flere i følgende liste har ulik verdi i forhold til de andre: [(RESULT_TOT_1, 9), (AVTALE_TOT_TOT, 1)]",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(sum: String) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.RESULT_TOT_1_COL_NAME to sum,
                    Familievern55ColumnNames.AVTALE_TOT_TOT_COL_NAME to "1",
                )
            )
        )
    }
}