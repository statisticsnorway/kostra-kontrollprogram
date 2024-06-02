package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.testutil.RandomUtils.generateRandomSsn
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoContextTest
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest

class Rule006AlderUnder18AarTest : BehaviorSpec({
    include(
        validationRuleNoContextTest(
            sut = Rule006AlderUnder18Aar(),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "record with valid age",
                kostraRecordInTest(generateRandomSsn(18, argumentsInTest.aargang.toInt())),
            ),
            ForAllRowItem(
                "record with blank fødselsnummer",
                kostraRecordInTest(" ".repeat(11)),
            ),
            ForAllRowItem(
                "record with invalid age",
                kostraRecordInTest(generateRandomSsn(17, argumentsInTest.aargang.toInt())),
                expectedErrorMessage = "Deltakeren (17 år) er under 18 år.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(foedselsnummer: String) = listOf(
            mapOf(
                PERSON_FODSELSNR_COL_NAME to foedselsnummer,
                SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                PERSON_JOURNALNR_COL_NAME to "~journalId~"
            ).toKostraRecord(lineNumber = 1, fieldDefinitions = fieldDefinitions)
        )
    }
}
