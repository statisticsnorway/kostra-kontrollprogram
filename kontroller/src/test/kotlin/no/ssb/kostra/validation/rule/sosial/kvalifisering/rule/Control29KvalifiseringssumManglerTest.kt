package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Control29KvalifiseringssumManglerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Control29KvalifiseringssumMangler(),
            forAllRows = listOf(
                ForAllRowItem(
                    "valid kvpStonad",
                    kostraRecordInTest("1")
                ),
                ForAllRowItem(
                    "empty kvpStonad",
                    kostraRecordInTest(" "),
                    "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                            "kvalifiseringsstønad ( ) i løpet av året, eller feltet inneholder andre tegn enn " +
                            "tall. Feltet er obligatorisk å fylle ut."
                )
            ),
            expectedSeverity = Severity.WARNING
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(kvpStonad: String) =
            kvalifiseringKostraRecordInTest(mapOf(KVP_STONAD_COL_NAME to kvpStonad))
    }
}
