package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule013AntallBarnUnder18Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule013AntallBarnUnder18(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "numberOfChildren = 13",
                kostraRecordInTest(13),
            ),
            ForAllRowItem(
                "numberOfChildren = 14",
                kostraRecordInTest(14),
                expectedErrorMessage = "Antall barn (14) under 18 år i husholdningen er 14 " +
                        "eller flere, er dette riktig?",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(numberOfChildren: Int) = listOf(
            kvalifiseringKostraRecordInTest(mapOf(ANT_BARN_UNDER_18_COL_NAME to numberOfChildren.toString()))
        )
    }
}
