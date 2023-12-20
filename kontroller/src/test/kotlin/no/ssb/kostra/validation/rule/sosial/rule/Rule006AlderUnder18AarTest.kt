package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpColumnNames
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.sosialhjelp.rule.Rule006AlderUnder18Aar

class Rule006AlderUnder18AarTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule006AlderUnder18Aar(),
            expectedSeverity = Severity.WARNING,
            ForAllRowItem(
                "record with valid age",
                kostraRecordInTest(RandomUtils.generateRandomSsn(18, RuleTestData.argumentsInTest.aargang.toInt())),
            ),
            ForAllRowItem(
                "record with blank fødselsnummer",
                kostraRecordInTest(" ".repeat(11)),
            ),
            ForAllRowItem(
                "record with invalid age",
                kostraRecordInTest(RandomUtils.generateRandomSsn(17, RuleTestData.argumentsInTest.aargang.toInt())),
                expectedErrorMessage = "Deltakeren (17 år) er under 18 år.",
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(foedselsnummer: String) = listOf(
            mapOf(
                SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to foedselsnummer,
                SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalId~"
            ).toKostraRecord(lineNumber = 1, fieldDefinitions = SosialhjelpFieldDefinitions.fieldDefinitions)
        )
    }
}
