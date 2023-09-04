package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule014FodselsaarTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule014Fodselsaar(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid age",
                kostraRecordInTest("2022"),
            ),
            ForAllRowItem(
                "invalid age, in the future",
                kostraRecordInTest("2029"),
                expectedErrorMessage = "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har " +
                        "ugyldig format, årgang gir negativ alder, eller alder større enn 100 år. Fant '2029'. " +
                        "Feltet er obligatorisk å fylle ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har " +
                        "korrekt format og logisk verdi i forhold til alder.",
            ),
            ForAllRowItem(
                "invalid age, too far in the past",
                kostraRecordInTest("1900"),
                expectedErrorMessage = "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har " +
                        "ugyldig format, årgang gir negativ alder, eller alder større enn 100 år. Fant '1900'. " +
                        "Feltet er obligatorisk å fylle ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har " +
                        "korrekt format og logisk verdi i forhold til alder.",
            ),
            ForAllRowItem(
                "invalid age, missing value",
                kostraRecordInTest("    "),
                expectedErrorMessage = "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har " +
                        "ugyldig format, årgang gir negativ alder, eller alder større enn 100 år. Fant '    '. " +
                        "Feltet er obligatorisk å fylle ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har " +
                        "korrekt format og logisk verdi i forhold til alder.",
            ),
            ForAllRowItem(
                "invalid age, illegal characters",
                kostraRecordInTest("FAIL"),
                expectedErrorMessage = "Dette er ikke oppgitt fødselsår på primærklienten eller feltet har " +
                        "ugyldig format, årgang gir negativ alder, eller alder større enn 100 år. Fant 'FAIL'. " +
                        "Feltet er obligatorisk å fylle ut. Fyll inn fødselsår eller sjekk at fødselsårsfeltet har " +
                        "korrekt format og logisk verdi i forhold til alder.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(age: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.PRIMK_FODT_A_COL_NAME to age)
            )
        )
    }
}