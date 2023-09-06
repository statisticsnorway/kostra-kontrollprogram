package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule009KontaktTidligereTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule009KontaktTidligere(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut om primærklienten har vært i kontakt med/klient ved " +
                        "familievernet tidligere, eller feil kode er benyttet. Fant 'X', forventet én av: [" +
                        "1=under 6 md siden, 2=mellom 6 md og 3 år siden, 3=3 år eller mer siden, " +
                        "4=har ikke vært i kontakt med familievernet tidligere]. " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.KONTAKT_TIDL_A_COL_NAME to code)
            )
        )
    }
}