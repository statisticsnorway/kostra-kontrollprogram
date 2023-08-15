package no.ssb.kostra.validation.rule.famvern.famvern52b

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52b.Familievern52bColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule006DubletterTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule006Dubletter(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "No duplicates, different group Ids",
                listOf(
                    kostraRecordInTest("017", "G1"),
                    kostraRecordInTest("017", "G2")
                ),
            ),
            ForAllRowItem(
                "No duplicates, different groups and offices",
                listOf(
                    kostraRecordInTest("017", "G1"),
                    kostraRecordInTest("023", "G1")
                ),
            ),
            ForAllRowItem(
                "Duplicates",
                listOf(
                    kostraRecordInTest("017", "G1"),
                    kostraRecordInTest("017", "G1")
                ),
                expectedErrorMessage = "Gruppenummeret er benyttet på mer enn en sak (2 stk). " +
                        "Dubletter på kontornummer '017' - gruppenummer 'G1'.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(kontor: String, gruppeNummer: String) =
            Familievern52bTestUtils.familievernRecordInTest(
                mapOf(
                    Familievern52bColumnNames.KONTOR_NR_B_COL_NAME to kontor,
                    Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME to gruppeNummer,
                )
            )
    }
}