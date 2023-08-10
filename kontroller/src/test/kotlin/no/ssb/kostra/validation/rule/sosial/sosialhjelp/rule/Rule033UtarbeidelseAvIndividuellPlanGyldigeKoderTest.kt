package no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.SosialhjelpTestUtils

class Rule033UtarbeidelseAvIndividuellPlanGyldigeKoderTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule033UtarbeidelseAvIndividuellPlanGyldigeKoder(),
            forAllRows = listOf(
                ForAllRowItem(
                    "code = 1",
                    kostraRecordInTest("1"),
                ),
                ForAllRowItem(
                    "code = X",
                    kostraRecordInTest("X"),
                    expectedErrorMessage = "Det er ikke krysset av for om mottakeren har fått utarbeidet individuell " +
                            "plan. Utfylt verdi er '(X=Ukjent)'. Feltet er obligatorisk å fylle ut.",
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
                FAAT_INDIVIDUELL_PLAN_COL_NAME to code
            )
        )
    }
}