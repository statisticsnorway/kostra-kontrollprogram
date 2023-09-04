package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule040VilkarSamEktTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule040VilkarSamEkt(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "code = 1",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "code = X",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke krysset av for om det stilles vilkår til mottakeren etter " +
                        "sosialtjenesteloven. Registreres for første vilkår i kalenderåret. Feltet er obligatorisk.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            code: String
        ) = SosialhjelpTestUtils.sosialKostraRecordInTest(
            mapOf(
                VILKARSAMEKT_COL_NAME to code
            )
        )
    }
}