package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule015SamlivsstatusTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule015Samlivsstatus(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Primærklientens samlivsstatus ved sakens opprettelse er ikke fylt ut eller " +
                        "feil kode er benyttet. Fant 'X', forventet én av: [1=Gift, 2=Registrert partner, " +
                        "3=Samboer, 4=Lever ikke i samliv]. Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.PRIMK_SIVILS_A_COL_NAME to code)
            )
        )
    }
}