package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule018ArbeidssituasjonTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule018Arbeidssituasjon(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke krysset av for primærklientens tilknytning til arbeidslivet ved " +
                        "sakens opprettelse eller feil kode er benyttet. Fant 'X', forventet én av: [" +
                        "1=Arbeid heltid, 2=Arbeid deltid, 3=Arbeidssøker, 4=Under utdanning, " +
                        "5=Fødselspermisjon / Fedrekvote, 6=Annen inntekt fre NAV, 7=Uten inntekt]. " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.PRIMK_ARBSIT_A_COL_NAME to code)
            )
        )
    }
}