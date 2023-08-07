package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule036StatusForDeltakelseIKvalifiseringsprogramTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule036StatusForDeltakelseIKvalifiseringsprogram(),
            forAllRows = listOf(
                *(1..6).map {
                    ForAllRowItem(
                        "valid status, $it",
                        kostraRecordInTest(it.toString()),
                    )
                }.toTypedArray(),
                ForAllRowItem(
                    "invalid status",
                    kostraRecordInTest("7"),
                    "Korrigér status. Fant '7', forventet én av",
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String) = listOf(
            kvalifiseringKostraRecordInTest(mapOf(STATUS_COL_NAME to status))
        )
    }
}
