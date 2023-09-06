package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule036AvslutningsdatoForForsteSamtaleTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule036AvslutningsdatoForForsteSamtale(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "all good",
                kostraRecordInTest("01012023", "02022023"),
            ),
            ForAllRowItem(
                "invalid order",
                kostraRecordInTest("02022023", "01012023"),
                expectedErrorMessage = "Dato for avslutting av saken '01012023' kommer før " +
                        "dato for første behandlingssamtale '02022023'."
            ),
            ForAllRowItem(
                "invalid date, invalid day",
                kostraRecordInTest("32102023", "02022023"),
                expectedErrorMessage = "Dato for avslutting av saken '02022023' kommer før " +
                        "dato for første behandlingssamtale '32102023'.",
            ),
            ForAllRowItem(
                "invalid date, illegal characters",
                kostraRecordInTest("XXXXXXXX", "02022023"),
                expectedErrorMessage = "Dato for avslutting av saken '02022023' kommer før " +
                        "dato for første behandlingssamtale 'XXXXXXXX'.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(samtaleDato: String, avslutningsdato: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME to samtaleDato,
                    Familievern52aColumnNames.DATO_AVSL_A_COL_NAME to avslutningsdato,
                )
            )
        )
    }
}