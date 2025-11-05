package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData

class Rule007AlderErOver68AarTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule007AlderErOver68Aar(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "record with valid age",
                kostraRecordInTest(
                    RandomUtils.generateRandomSsn(
                        68,
                        RuleTestData.argumentsInTest.aargang.toInt()
                    )
                ),
            ),
            ForAllRowItem(
                "record with blank fødselsnummer",
                kostraRecordInTest(" ".repeat(11)),
            ),
            ForAllRowItem(
                "record with invalid age",
                kostraRecordInTest(
                    RandomUtils.generateRandomSsn(
                        70,
                        RuleTestData.argumentsInTest.aargang.toInt()
                    )
                ),
                expectedErrorMessage = "Deltakeren (70 år) er over 68 år.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(foedselsnummer: String) = listOf(
            mapOf(
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to foedselsnummer,
                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalId~"
            ).toKostraRecord(lineNumber = 1, fieldDefinitions = KvalifiseringFieldDefinitions.fieldDefinitions)
        )
    }
}