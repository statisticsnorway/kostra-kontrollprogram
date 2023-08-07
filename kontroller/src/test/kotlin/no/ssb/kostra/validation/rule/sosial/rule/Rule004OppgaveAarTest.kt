package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule004OppgaveAarTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule004OppgaveAar(),
            forAllRows = listOf(
                ForAllRowItem(
                    "record with valid aargang",
                    kostraRecordInTest((argumentsInTest.aargang.toInt() - 2_000).toString()),
                ),
                ForAllRowItem(
                    "record with non-numeric aargang",
                    kostraRecordInTest("ab"),
                    expectedErrorMessage = "Korrigér årgang. Fant ab, forventet",
                ),
                ForAllRowItem(
                    "record with invalid aargang",
                    kostraRecordInTest("42"),
                    expectedErrorMessage = "Korrigér årgang. Fant 42, forventet",
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(version: String) =
            listOf(
                mapOf(
                    VERSION_COL_NAME to version,
                    PERSON_FODSELSNR_COL_NAME to "~fodselsnr~",
                    SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                    PERSON_JOURNALNR_COL_NAME to "~journalId~"
                ).toKostraRecord(lineNumber = 1, fieldDefinitions = fieldDefinitions)
            )
    }
}
