package no.ssb.kostra.validation.rule.famvern.famvern55

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern55.Familievern55ColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule019VoldSomTemaTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule019VoldSomTema(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid numbers",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid numbers",
                kostraRecordInTest("4"),
                expectedErrorMessage = "Summen (VOLD_TOT) med verdi (4) er ulik summen (1) av følgende liste ([(VOLD_TEMA, 1), (VOLD_IKKE_TEMA, 0)])",
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
                    Familievern55ColumnNames.VOLD_TEMA_COL_NAME to "1",
                    Familievern55ColumnNames.VOLD_TOT_COL_NAME to sumOfSums,
                )
            )
        )
    }
}