package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule020DatoForsteBehandlingssamtaleTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule020DatoForsteBehandlingssamtale(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid date",
                kostraRecordInTest("01012023"),
            ),
            ForAllRowItem(
                "invalid date, invalid day",
                kostraRecordInTest("32102023"),
                expectedErrorMessage = "Det er ikke oppgitt dato for første behandlingssamtale eller feltet har " +
                        "ugyldig format. Fant '32102023'. Feltet er obligatorisk å fylle ut."
            ),
            ForAllRowItem(
                "invalid date, illegal characters",
                kostraRecordInTest("XXXXXXXX"),
                expectedErrorMessage = "Det er ikke oppgitt dato for første behandlingssamtale eller feltet har " +
                        "ugyldig format. Fant 'XXXXXXXX'. Feltet er obligatorisk å fylle ut."
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(dato: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME to dato)
            )
        )
    }
}