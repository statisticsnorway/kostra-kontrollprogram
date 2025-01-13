package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule021AvslutningsdatoForForsteSamtaleTest :
    BehaviorSpec({
        include(
            KostraTestFactory.validationRuleNoContextTest(
                sut = Rule021AvslutningsdatoForForsteSamtale(),
                expectedSeverity = Severity.WARNING,
                ForAllRowItem(
                    "all good",
                    kostraRecordInTest("01012023", "02022023"),
                ),
                ForAllRowItem(
                    "same day",
                    kostraRecordInTest("01012023", "01012023"),
                ),
                ForAllRowItem(
                    "invalid order",
                    kostraRecordInTest("02022023", "01012023"),
                    expectedErrorMessage =
                        "Dato for avslutting av gruppebehandlingen '01012023' kommer " +
                            "før dato for gruppebehandlingens start '02022023'.",
                ),
                ForAllRowItem(
                    "invalid date, invalid day",
                    kostraRecordInTest("32102023", "02022023"),
                    expectedErrorMessage =
                        "Dato for avslutting av gruppebehandlingen '02022023' kommer " +
                            "før dato for gruppebehandlingens start '32102023'.",
                ),
                ForAllRowItem(
                    "invalid date, illegal characters",
                    kostraRecordInTest("XXXXXXXX", "02022023"),
                    expectedErrorMessage =
                        "Dato for avslutting av gruppebehandlingen '02022023' kommer " +
                            "før dato for gruppebehandlingens start 'XXXXXXXX'.",
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordInTest(
            samtaleDato: String,
            avslutningsdato: String,
        ) = listOf(
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52bColumnNames.DATO_GRSTART_B_COL_NAME to samtaleDato,
                    Familievern52bColumnNames.DATO_GRAVSLUTN_B_COL_NAME to avslutningsdato,
                ),
            ),
        )
    }
}
