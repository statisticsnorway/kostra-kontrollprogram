package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule040VilkarSamEktTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule040VilkarSamEkt(),
            forAllRows = listOf(
                ForAllRowItem(
                    "code = 1",
                    kostraRecordInTest("1"),
                ),
                ForAllRowItem(
                    "code = X",
                    kostraRecordInTest("X"),
                    expectedErrorMessage = "Det er ikke krysset av for om det stilles vilkår til mottakeren etter " +
                            "sosialtjenesteloven. Registreres for første vilkår i kalenderåret. Feltet er obligatorisk.",
                ),
            ),
            expectedSeverity = Severity.ERROR
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