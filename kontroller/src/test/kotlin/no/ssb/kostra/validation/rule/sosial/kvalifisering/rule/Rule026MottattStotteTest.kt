package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringTestUtils.kvalifiseringKostraRecordInTest

class Rule026MottattStotteTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule026MottattStotte(),
            forAllRows = listOf(
                ForAllRowItem(
                    "valid kvpMedAStonad, 1",
                    kostraRecordInTest(1),
                ),
                ForAllRowItem(
                    "valid kvpMedAStonad, 2",
                    kostraRecordInTest(2),
                ),
                ForAllRowItem(
                    "invalid kvpMedAStonad",
                    kostraRecordInTest(42),
                    "Feltet for 'Har deltakeren i ${argumentsInTest.aargang} i løpet av perioden med " +
                            "kvalifiseringsstønad også mottatt  økonomisk sosialhjelp, kommunal bostøtte eller " +
                            "Husbankens bostøtte?', er ikke utfylt eller feil kode (42) er benyttet. Feltet er " +
                            "obligatorisk å fylle ut.",
                )
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(kvpMedAStonad: Int) = listOf(
            kvalifiseringKostraRecordInTest(mapOf(KVP_MED_ASTONAD_COL_NAME to kvpMedAStonad.toString()))
        )
    }
}
