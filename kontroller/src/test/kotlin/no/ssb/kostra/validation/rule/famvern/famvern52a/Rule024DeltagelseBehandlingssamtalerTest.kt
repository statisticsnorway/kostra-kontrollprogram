package no.ssb.kostra.validation.rule.famvern.famvern52a

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule024DeltagelseBehandlingssamtalerTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule024DeltagelseBehandlingssamtaler(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "valid code, participant",
                kostraRecordInTest("1"),
            ),
            ForAllRowItem(
                "valid code, no participant",
                kostraRecordInTest("2"),
                expectedErrorMessage = "Det er ikke krysset av for om andre deltakere i saken har deltatt i samtaler " +
                        "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "missing code",
                kostraRecordInTest(" "),
                expectedErrorMessage = "Det er ikke krysset av for om andre deltakere i saken har deltatt i samtaler " +
                        "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut.",
            ),
            ForAllRowItem(
                "invalid code, illegal characters",
                kostraRecordInTest("X"),
                expectedErrorMessage = "Det er ikke krysset av for om andre deltakere i saken har deltatt i samtaler " +
                        "med primærklienten i løpet av rapporteringsåret. Feltene er obligatorisk å fylle ut.",
            ),
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(tema: String) = listOf(
            Familievern52aTestUtils.familievernRecordInTest(
                mapOf(Familievern52aColumnNames.DELT_PARTNER_A_COL_NAME to tema)
            )
        )
    }
}