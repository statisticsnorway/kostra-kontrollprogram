package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule011HenvendelsesBegrunnelseTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule011HenvendelsesBegrunnelse(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Dette er ikke oppgitt hva som er primærklientens viktigste ønsker med " +
                        "kontakten, eller feltet har ugyldig format. Fant 'X', forventet én av: [" +
                        "1=Parforholdet, 2=Foreldresamarbeid/- veiledning, " +
                        "3=Andre eller sammensatte problemer i familien, 4=Hjelp til barn og ungdom]. " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.HENV_GRUNN_A_COL_NAME to code)
            )
        )
    }
}