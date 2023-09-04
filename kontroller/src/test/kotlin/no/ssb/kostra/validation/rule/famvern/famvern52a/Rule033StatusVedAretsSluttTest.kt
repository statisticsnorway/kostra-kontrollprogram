package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule033StatusVedAretsSluttTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule033StatusVedAretsSlutt(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hva som er sakens status ved utgangen av året. " +
                        "Fant 'X', forventet én av: [1=Avsluttet etter avtale med klient, 2=Klient uteblitt, " +
                        "3=Saken ikke avsluttet i inneværende år]. Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME to code)
            )
        )
    }
}