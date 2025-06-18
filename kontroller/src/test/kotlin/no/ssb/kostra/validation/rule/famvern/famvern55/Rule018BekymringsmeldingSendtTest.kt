package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule018BekymringsmeldingSendtTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule018BekymringsmeldingSendt(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("4"),
                expectedErrorMessage = "Summen (BEKYMR_TOT) med verdi (4) er ulik summen (1) av følgende liste ([(BEKYMR_SENDT, 1), (BEKYMR_IKKE_SENDT, 0)])",
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
                    Familievern55ColumnNames.BEKYMR_SENDT_COL_NAME to "1",
                    Familievern55ColumnNames.BEKYMR_TOT_COL_NAME to sumOfSums,
                )
            )
        )
    }
}