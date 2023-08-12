package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule012AntallBarnUnder18MotHarBarnUnder18Test : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule012AntallBarnUnder18MotHarBarnUnder18(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "bu18Code = 0, numberOfChildren = 0",
                kostraRecordInTest(0, 0),
            ),
            ForAllRowItem(
                "bu18Code = 1, numberOfChildren = 1",
                kostraRecordInTest(1, 1),
            ),
            ForAllRowItem(
                "bu18Code = 1, numberOfChildren = 0",
                kostraRecordInTest(1, 0),
            ),
            ForAllRowItem(
                "bu18Code = 0, numberOfChildren = 1",
                kostraRecordInTest(0, 1),
                expectedErrorMessage = "Det er oppgitt 1 barn under 18 år som bor i husholdningen som " +
                        "mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke oppgitt at " +
                        "det bor barn i husholdningen. Feltet er obligatorisk å fylle ut når det er oppgitt " +
                        "antall barn under 18 år som bor i husholdningen.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            bu18Code: Int,
            numberOfChildren: Int
        ) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    HAR_BARN_UNDER_18_COL_NAME to bu18Code.toString(),
                    ANT_BARN_UNDER_18_COL_NAME to numberOfChildren.toString()
                )
            )
        )
    }
}
