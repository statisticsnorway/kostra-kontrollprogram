package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule016FormellSivilstandTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule016FormellSivilstand(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "different sivilstand, no match",
                kostraRecordInTest("X", " "),
            ),
            ForAllRowItem(
                "all good",
                kostraRecordInTest("3", "1"),
            ),
            ForAllRowItem(
                "sivilstand = OK, invalid formell sivilstand",
                kostraRecordInTest("3", " "),
                expectedErrorMessage = "Det er oppgitt at primærklientens samlivsstatus er Samboer eller at " +
                        "primærklient ikke lever i samliv, men det er ikke fylt ut hva som er primærklientens " +
                        "korrekt sivilstand. Fant ' ', forventet én av: [1=Ugift, 2=Gift, 3=Registrert partner, " +
                        "4=Separert / separert partner, 5=Skilt / skilt partner, " +
                        "6=Enke / enkemann / gjenlevende partner].",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(sivilstand: String, formellSivilstand: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.PRIMK_SIVILS_A_COL_NAME to sivilstand,
                    Familievern52aColumnNames.FORMELL_SIVILS_A_COL_NAME to formellSivilstand
                )
            )
        )
    }
}