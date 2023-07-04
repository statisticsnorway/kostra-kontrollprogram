package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control11Bu18AntBu18Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control11Bu18AntBu18(),
            forAllRows = listOf(
                ForAllRowItem(
                    "bu18Code = 0, numberOfChildren = 0",
                    kostraRecordInTest(0, 0)
                ),
                ForAllRowItem(
                    "bu18Code = 1, numberOfChildren = 1",
                    kostraRecordInTest(1, 1)
                ),
                ForAllRowItem(
                    "bu18Code = 0, numberOfChildren = 1",
                    kostraRecordInTest(0, 1)
                ),
                ForAllRowItem(
                    "bu18Code = 1, numberOfChildren = 0",
                    kostraRecordInTest(1, 0),
                    expectedErrorMessage = "Det er krysset av for at det bor barn under 18 år i husholdningen " +
                            "som mottaker eller ektefelle/samboer har forsørgerplikt for, men det er ikke " +
                            "oppgitt hvor mange barn '(0)' som bor i husholdningen. Feltet er obligatorisk å " +
                            "fylle ut når det er oppgitt at det bor barn under 18 år i husholdningen."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            bu18Code: Int,
            numberOfChildren: Int
        ) = kvalifiseringKostraRecordInTest(
            mapOf(
                HAR_BARN_UNDER_18_COL_NAME to bu18Code.toString(),
                ANT_BARN_UNDER_18_COL_NAME to numberOfChildren.toString()
            )
        )
    }
}
