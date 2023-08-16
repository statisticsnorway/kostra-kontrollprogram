package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule006AvsluttedeMeklingerEtterDeltakereTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule006AvsluttedeMeklingerEtterDeltakere(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1", "1", "1", "1"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("1", "2", "3", "4"),
                expectedErrorMessage = "Summen (SEP_TOT) med verdi (2) er ulik summen (1) av følgende liste ([(SEP_BEGGE, 1), (SEP_EN, 0)])",
                expectedSize = 4
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            number: String,
            rowsum: String,
            columnSum: String,
            sumOfSums: String
        ) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.SEP_BEGGE_COL_NAME to number,
                    Familievern55ColumnNames.SEP_TOT_COL_NAME to rowsum,
                    Familievern55ColumnNames.BEGGE_TOT_COL_NAME to columnSum,
                    Familievern55ColumnNames.ENBEGGE_TOT_COL_NAME to sumOfSums
                )
            )
        )
    }
}