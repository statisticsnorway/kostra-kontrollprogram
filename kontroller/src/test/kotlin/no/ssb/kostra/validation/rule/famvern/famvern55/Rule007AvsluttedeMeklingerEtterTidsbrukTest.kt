package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule007AvsluttedeMeklingerEtterTidsbrukTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule007AvsluttedeMeklingerEtterTidsbruk(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1", "1", "1", "1"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("1", "2", "3", "4"),
                expectedErrorMessage = "Summen (VENTETID_SEP_TOT) med verdi (2) er ulik summen (1) av følgende liste ([(VENTETID_SEP_1, 1), (VENTETID_SEP_2, 0), (VENTETID_SEP_3, 0), (VENTETID_SEP_4, 0)])",
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
                    Familievern55ColumnNames.VENTETID_SEP_1_COL_NAME to number,
                    Familievern55ColumnNames.VENTETID_SEP_TOT_COL_NAME to rowsum,
                    Familievern55ColumnNames.VENTETID_TOT_1_COL_NAME to columnSum,
                    Familievern55ColumnNames.VENTETID_TOT_TOT_COL_NAME to sumOfSums
                )
            )
        )
    }
}