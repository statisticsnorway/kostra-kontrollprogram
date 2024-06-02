package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule029KvalifiseringssumManglerTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule029KvalifiseringssumMangler(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "Status = X, kvpStonad = 0",
                kostraRecordInTest("X", "0"),
            ),
            ForAllRowItem(
                "Status = 1, kvpStonad = 1",
                kostraRecordInTest("1", "1"),
            ),
            ForAllRowItem(
                "Status = 1, kvpStonad = 0",
                kostraRecordInTest("1", "0"),
                "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                        "kvalifiseringsstønad (0) i løpet av året, eller feltet inneholder andre tegn enn " +
                        "tall."
            ),
            ForAllRowItem(
                "Status = 1, kvpStonad is empty",
                kostraRecordInTest("1", " "),
                "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                        "kvalifiseringsstønad () i løpet av året, eller feltet inneholder andre tegn enn " +
                        "tall.",
            ),
            ForAllRowItem(
                "Status = 1, kvpStonad is invalid",
                kostraRecordInTest("1", "X"),
                "Det er ikke oppgitt hvor mye deltakeren har fått i " +
                        "kvalifiseringsstønad (X) i løpet av året, eller feltet inneholder andre tegn enn " +
                        "tall.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, kvpStonad: String) = listOf(
            kvalifiseringKostraRecordInTest(mapOf(STATUS_COL_NAME to status, KVP_STONAD_COL_NAME to kvpStonad))
        )
    }
}
