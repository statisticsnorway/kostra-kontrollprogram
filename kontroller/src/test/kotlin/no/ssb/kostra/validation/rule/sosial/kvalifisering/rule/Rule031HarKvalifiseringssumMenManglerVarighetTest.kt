package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.SosialConstants.MONTH_PREFIX
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule031HarKvalifiseringssumMenManglerVarighetTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule031HarKvalifiseringssumMenManglerVarighet(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "with other status, months and amount",
                kostraRecordInTest("X", " ", false),
            ),
            ForAllRowItem(
                "with months and amount",
                kostraRecordInTest("1", "2", true),
            ),
            ForAllRowItem(
                "without months, without amount",
                kostraRecordInTest("1", " ", false),
            ),
            ForAllRowItem(
                "without months, with amount",
                kostraRecordInTest("1", "1", false),
                "Deltakeren har fått kvalifiseringsstønad (1) i løpet av året, " +
                        "men mangler utfylling for hvilke måneder stønaden gjelder. " +
                        "Feltet er obligatorisk å fylle ut.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, kvpStonad: String, varighet: Boolean) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    STATUS_COL_NAME to status,
                    KVP_STONAD_COL_NAME to kvpStonad,
                    *((1..12).map {
                        "$MONTH_PREFIX$it" to if (varighet) it.toString().padStart(2, '0') else ""
                    }).toTypedArray()
                )
            )
        )
    }
}
