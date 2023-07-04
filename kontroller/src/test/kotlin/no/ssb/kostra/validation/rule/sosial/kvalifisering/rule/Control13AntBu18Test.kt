package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control13AntBu18Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control13AntBu18(),
            forAllRows = listOf(
                ForAllRowItem(
                    "numberOfChildren = 13",
                    kostraRecordInTest(13)
                ),
                ForAllRowItem(
                    "numberOfChildren = 14",
                    kostraRecordInTest(14),
                    expectedErrorMessage = "Antall barn (14) under 18 år i husholdningen er 14 " +
                            "eller flere, er dette riktig?"
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(numberOfChildren: Int) =
            kvalifiseringKostraRecordInTest(mapOf(ANT_BARN_UNDER_18_COL_NAME to numberOfChildren.toString()))
    }
}
