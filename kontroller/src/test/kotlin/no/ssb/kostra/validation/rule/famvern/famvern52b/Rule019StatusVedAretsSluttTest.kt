package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule019StatusVedAretsSluttTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule019StatusVedAretsSlutt(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hva som er gruppens status ved utgangen av året. " +
                        "Fant 'X', forventet én av: [1=Gruppebehandlingen ikke avsluttet i inneværende år, " +
                        "2=Avsluttet].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.STATUS_ARETSSL_B_COL_NAME to code)
            )
        )
    }
}