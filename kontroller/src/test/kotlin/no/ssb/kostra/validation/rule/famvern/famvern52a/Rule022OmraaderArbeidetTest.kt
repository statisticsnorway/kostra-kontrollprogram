package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule022OmraaderArbeidetTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule022OmraaderArbeidet(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "missing code",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke fylt ut hvilke områder det har vært arbeidet med siden saken ble opprettet. " +
                        "Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område.",
            ),
            ForAllRowItem(
                "invalid code, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke fylt ut hvilke områder det har vært arbeidet med siden saken ble opprettet. " +
                        "Feltet er obligatorisk å fylle ut, og kan inneholde mer enn ett område.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(tema: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.TEMA_PARREL_A_COL_NAME to tema)
            )
        )
    }
}