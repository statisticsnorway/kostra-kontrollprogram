package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule019KvalifiseringsprogramIAnnenKommuneTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule019KvalifiseringsprogramIAnnenKommune(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "code = 1",
                kostraRecordInTest(1),
            ),
            ForAllRowItem(
                "code = 2",
                kostraRecordInTest(2),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest(42),
                expectedErrorMessage = "Feltet for 'Kommer deltakeren fra kvalifiseringsprogram i " +
                        "annen kommune?' er ikke fylt ut, eller feil kode er benyttet (42). " +
                        "Feltet er obligatorisk å fylle ut.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: Int) = listOf(
            kvalifiseringKostraRecordInTest(mapOf(KVP_KOMM_COL_NAME to code.toString()))
        )
    }
}
