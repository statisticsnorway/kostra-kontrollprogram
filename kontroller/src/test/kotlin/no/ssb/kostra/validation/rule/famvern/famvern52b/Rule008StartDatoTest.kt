package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule008StartDatoTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule008StartDato(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid date",
                kostraRecordInTest("01012023"),
            ),
            ForAllRowItem(
                "invalid date, invalid day",
                kostraRecordInTest("32102023"),
                expectedErrorMessage = "Det er ikke oppgitt dato for gruppebehandlingens start på " +
                        "formatet DDMMÅÅÅÅ. Fant '32102023'.",
            ),
            ForAllRowItem(
                "invalid date, illegal characters",
                kostraRecordInTest("XXXXXXXX"),
                expectedErrorMessage = "Det er ikke oppgitt dato for gruppebehandlingens start på " +
                        "formatet DDMMÅÅÅÅ. Fant 'XXXXXXXX'.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(dato: String) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(Familievern52bColumnNames.DATO_GRSTART_B_COL_NAME to dato)
            )
        )
    }
}