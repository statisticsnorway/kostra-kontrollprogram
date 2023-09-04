package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule017BosituasjonTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule017Bosituasjon(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut om primærklienten bor sammen med andre ved sakens " +
                        "opprettelse eller feil kode er benyttet. Fant 'X', forventet én av: [" +
                        "1=Partner (og eventuelt barn), 2=Barn, 3=Foreldre / Andre omsorgspersoner, 4=Andre, " +
                        "5=Ikke sammen med andre]. Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.PRIMK_SAMBO_A_COL_NAME to code)
            )
        )
    }
}