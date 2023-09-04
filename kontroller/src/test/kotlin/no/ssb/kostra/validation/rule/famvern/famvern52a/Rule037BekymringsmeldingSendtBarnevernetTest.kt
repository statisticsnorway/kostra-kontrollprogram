package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule037BekymringsmeldingSendtBarnevernetTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule037BekymringsmeldingSendtBarnevernet(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "invalid code",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke svart på hvorvidt bekymringsmelding er sendt barnevernet " +
                        "eller ei, eller feil kode er benyttet. Fant 'X', forventet én av: [1=Ja, 2=Nei]. " +
                        "Feltet er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(code: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.BEKYMR_MELD_A_COL_NAME to code)
            )
        )
    }
}