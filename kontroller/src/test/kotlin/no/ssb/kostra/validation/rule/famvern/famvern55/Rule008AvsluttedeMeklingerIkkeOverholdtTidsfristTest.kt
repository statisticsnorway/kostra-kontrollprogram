package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule008AvsluttedeMeklingerIkkeOverholdtTidsfristTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule008AvsluttedeMeklingerIkkeOverholdtTidsfrist(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("2"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("3"),
                expectedErrorMessage = "Summen (FORHOLD_TOT) med verdi (3) er ulik summen (2) av følgende liste ([(FORHOLD_MEKLER, 1), (FORHOLD_KLIENT, 1)])",
                expectedSize = 1
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            rowSum: String
        ) = listOf(
            Familievern55TestUtils.familievernRecordInTest(
                mapOf(
                    Familievern55ColumnNames.FORHOLD_MEKLER_COL_NAME to "1",
                    Familievern55ColumnNames.FORHOLD_KLIENT_COL_NAME to "1",
                    Familievern55ColumnNames.FORHOLD_TOT_COL_NAME to rowSum
                )
            )
        )
    }
}