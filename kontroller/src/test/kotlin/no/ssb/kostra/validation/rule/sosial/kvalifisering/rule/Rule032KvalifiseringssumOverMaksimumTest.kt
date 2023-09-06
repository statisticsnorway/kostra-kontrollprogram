package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule032KvalifiseringssumOverMaksimum.Companion.STONAD_SUM_MAX

class Rule032KvalifiseringssumOverMaksimumTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule032KvalifiseringssumOverMaksimum(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "empty amount",
                kostraRecordInTest(" "),
            ),
            ForAllRowItem(
                "valid amount",
                kostraRecordInTest("42"),
            ),
            ForAllRowItem(
                "max amount",
                kostraRecordInTest(STONAD_SUM_MAX.toString()),
            ),
            ForAllRowItem(
                "amount too high",
                kostraRecordInTest((STONAD_SUM_MAX + 1).toString()),
                "Kvalifiseringsstønaden (600001) som deltakeren har fått i løpet av " +
                        "rapporteringsåret overstiger Statistisk sentralbyrås kontrollgrense på NOK 600000,-.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(amount: String) = listOf(
            kvalifiseringKostraRecordInTest(mapOf(KVP_STONAD_COL_NAME to amount))
        )
    }
}
