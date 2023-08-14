package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule019B1VarighetSidenBruddTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule019B1VarighetSidenBrudd(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "no match",
                kostraRecordInTest("X", " "),
            ),
            ForAllRowItem(
                "all good",
                kostraRecordInTest("1", "1"),
            ),
            ForAllRowItem(
                "invalid value",
                kostraRecordInTest("1", " "),
                expectedErrorMessage = "Det er oppgitt at primærklientens relasjon til viktigste deltager er " +
                        "ekspartner, men det er ikke oppgitt tid siden brudd. Fant ' ', forventet én av: [" +
                        "1=Har ikke bodd sammen, 2=Under 2 år, 3=2 - 4 år, 4=5 - 9 år, 5=10 - 19 år, " +
                        "6=20 år eller mer].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(relasjon: String, lengde: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.PRIMK_VSRELASJ_A_COL_NAME to relasjon,
                    Familievern52aColumnNames.EKSPART_LENGDE_A_COL_NAME to lengde
                )
            )
        )
    }
}