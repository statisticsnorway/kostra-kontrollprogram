package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.SosialConstants.MONTH_PREFIX
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringConstants.PERMISJON
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest


class Rule028MaanederMedKvalifiseringsstonadTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule028MaanederMedKvalifiseringsstonad(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "permisjon",
                kostraRecordInTest(PERMISJON, false),
            ),
            ForAllRowItem(
                "status != permisjon, all months present",
                kostraRecordInTest("1", true)
            ),
            ForAllRowItem(
                "status != permisjon, all months missing",
                kostraRecordInTest("1", false),
                expectedErrorMessage = "Det er ikke krysset av for hvilke måneder deltakeren har " +
                        "fått utbetalt kvalifiseringsstønad"
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(status: String, setValues: Boolean) = listOf(
            kvalifiseringKostraRecordInTest(
                mapOf(
                    KVP_STONAD_COL_NAME to "1000",
                    STATUS_COL_NAME to status,
                    *((1..12).map {
                        "$MONTH_PREFIX$it" to if (setValues) it.toString().padStart(2, '0') else " "
                    }).toTypedArray()
                )
            )
        )
    }
}
