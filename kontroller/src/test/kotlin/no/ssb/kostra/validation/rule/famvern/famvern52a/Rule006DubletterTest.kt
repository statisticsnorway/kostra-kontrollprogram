package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule006DubletterTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule006Dubletter(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "No duplicates, different journal Ids",
                listOf(
                    kostraRecordInTest("017", "J1"),
                    kostraRecordInTest("017", "J2")
                ),
            ),
            ForAllRowItem(
                "No duplicates, different journal offices",
                listOf(
                    kostraRecordInTest("017", "J1"),
                    kostraRecordInTest("023", "J1")
                ),
            ),
            ForAllRowItem(
                "Duplicates",
                listOf(
                    kostraRecordInTest("017", "J1"),
                    kostraRecordInTest("017", "J1")
                ),
                expectedErrorMessage = "Journalnummeret er benyttet på mer enn en sak (2 stk). " +
                        "Dubletter på kontornummer '017' - journalnummer 'J1'.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(kontor: String, journalNummer: String) =
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52aColumnNames.KONTOR_NR_A_COL_NAME to kontor,
                    Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME to journalNummer,
                )
            )
    }
}