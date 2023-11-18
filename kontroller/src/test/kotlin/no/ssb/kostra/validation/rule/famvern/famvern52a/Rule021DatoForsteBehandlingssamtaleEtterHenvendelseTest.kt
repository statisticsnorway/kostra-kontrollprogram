package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule021DatoForsteBehandlingssamtaleEtterHenvendelseTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule021DatoForsteBehandlingssamtaleEtterHenvendelse(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "all good, nothing to compare",
                kostraRecordInTest(" ".repeat(8), " ".repeat(8)),
            ),
            ForAllRowItem(
                "all good, no conversation",
                kostraRecordInTest("01012023", " ".repeat(8)),
            ),
            ForAllRowItem(
                "all good",
                kostraRecordInTest("01012023", "02012023"),
            ),
            ForAllRowItem(
                "same day",
                kostraRecordInTest("01012023", "01012023"),
            ),
            ForAllRowItem(
                "invalid order",
                kostraRecordInTest("02012023", "01012023"),
                expectedErrorMessage = "Dato for første behandlingssamtale '01012023' er før dato for " +
                        "primærklientens henvendelse '02012023' til familievernkontoret."
            ),
            ForAllRowItem(
                "invalid date, invalid day",
                kostraRecordInTest("32102023", "02022023"),
                expectedErrorMessage = "Dato for første behandlingssamtale '02022023' er før dato for " +
                        "primærklientens henvendelse '32102023' til familievernkontoret.",
            ),
            ForAllRowItem(
                "invalid date, illegal characters",
                kostraRecordInTest("XXXXXXXX", "02022023"),
                expectedErrorMessage = "Dato for første behandlingssamtale '02022023' er før dato for " +
                        "primærklientens henvendelse 'XXXXXXXX' til familievernkontoret.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(henvendelseDato: String, samtaleDato: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.HENV_DATO_A_COL_NAME to henvendelseDato,
                    Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME to samtaleDato
                )
            )
        )
    }
}