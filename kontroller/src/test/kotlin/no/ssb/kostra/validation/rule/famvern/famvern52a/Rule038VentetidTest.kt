package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule038VentetidTest: BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule038Ventetid(),
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
                "1 year",
                kostraRecordInTest("01012022", "01012023"),
            ),
            ForAllRowItem(
                "more than 1 year",
                kostraRecordInTest("31122021", "01012023"),
                expectedErrorMessage = "Dato for primærklientens henvendelse '31122021' til " +
                        "familievernkontoret er mer enn 1 år før første behandlingssamtale '01012023'."
            ),
            ForAllRowItem(
                "invalid date, invalid day of inquiry",
                kostraRecordInTest("32102023", "02022023"),
                expectedErrorMessage = "Dato for primærklientens henvendelse '32102023' til " +
                        "familievernkontoret er mer enn 1 år før første behandlingssamtale '02022023'."
            ),
            ForAllRowItem(
                "invalid date, invalid day of conversation",
                kostraRecordInTest("01012023", "32102023"),
                expectedErrorMessage = "Dato for primærklientens henvendelse '01012023' til " +
                        "familievernkontoret er mer enn 1 år før første behandlingssamtale '32102023'."
            ),
            ForAllRowItem(
                "invalid date, illegal characters",
                kostraRecordInTest("XXXXXXXX", "02022023"),
                expectedErrorMessage = "Dato for primærklientens henvendelse 'XXXXXXXX' til " +
                        "familievernkontoret er mer enn 1 år før første behandlingssamtale '02022023'."
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