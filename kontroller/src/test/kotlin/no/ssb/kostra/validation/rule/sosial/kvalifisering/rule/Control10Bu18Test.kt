package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control10Bu18Test : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control10Bu18(),
            forAllRows = listOf(
                ForAllRowItem(
                    "code = 1",
                    kostraRecordInTest(1)
                ),
                ForAllRowItem(
                    "code = 2",
                    kostraRecordInTest(1)
                ),

                ForAllRowItem(
                    "code = 3",
                    kostraRecordInTest(3),
                    expectedErrorMessage = "Korrigér forsørgerplikt. Fant '3', forventet én av [1=Ja, 2=Nei]'. " +
                            "Det er ikke krysset av for om deltakeren har barn under 18 år, som deltakeren " +
                            "(eventuelt ektefelle/samboer) har forsørgerplikt for, og som bor i husholdningen " +
                            "ved siste kontakt. Feltet er obligatorisk å fylle ut."
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: Int) = kvalifiseringKostraRecordInTest(
            mapOf(HAR_BARN_UNDER_18_COL_NAME to code.toString())
        )
    }
}
