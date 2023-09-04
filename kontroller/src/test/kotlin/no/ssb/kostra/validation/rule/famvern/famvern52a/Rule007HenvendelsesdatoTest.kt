package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule007HenvendelsesdatoTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule007Henvendelsesdato(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid date",
                kostraRecordInTest("01012023"),
            ),
            ForAllRowItem(
                "invalid date, invalid day",
                kostraRecordInTest("32102023"),
                expectedErrorMessage = "Dette er ikke oppgitt dato (32102023) for når primærklienten henvendte " +
                        "seg til familievernkontoret eller feltet har ugyldig format (DDMMÅÅÅÅ). " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "invalid date, illegal characters",
                kostraRecordInTest("XXXXXXXX"),
                expectedErrorMessage = "Dette er ikke oppgitt dato (XXXXXXXX) for når primærklienten henvendte " +
                        "seg til familievernkontoret eller feltet har ugyldig format (DDMMÅÅÅÅ). " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(dato: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.HENV_DATO_A_COL_NAME to dato)
            )
        )
    }
}