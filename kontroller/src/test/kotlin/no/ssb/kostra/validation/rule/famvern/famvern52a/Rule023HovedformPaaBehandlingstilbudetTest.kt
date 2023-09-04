package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule023HovedformPaaBehandlingstilbudetTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule023HovedformPaaBehandlingstilbudet(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke krysset av for hva som har vært hovedformen på " +
                        "behandlingstilbudet siden saken ble opprettet, eller feil kode er benyttet. Fant 'X', " +
                        "forventet én av: [1=Parsamtale, 2=Foreldresamtale, 3=Familiesamtale, 4=Individualsamtale]. " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.HOVEDF_BEHAND_A_COL_NAME to code)
            )
        )
    }
}