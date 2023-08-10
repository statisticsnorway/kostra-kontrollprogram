package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.LocalDate

class Rule005bJournalnummerDubletterTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule005bJournalnummerDubletter(),
            forAllRows = listOf(
                ForAllRowItem(
                    "no records",
                    emptyList(),
                ),
                ForAllRowItem(
                    "single record",
                    listOf(kostraRecordInTest()),
                ),
                ForAllRowItem(
                    "two records, different journalId",
                    listOf(
                        kostraRecordInTest(),
                        kostraRecordInTest(journalId = "~journalId2~")
                    ),
                ),
                ForAllRowItem(
                    "two records with same journalId",
                    listOf(
                        kostraRecordInTest(),
                        kostraRecordInTest()
                    ),
                    expectedErrorMessage = "Journalnummer ~journalId~ forekommer 2 ganger.",
                    expectedSize = 2
                ),
            ),
            expectedSeverity = Severity.ERROR
        )
    )

}) {
    companion object {
        private val fodselsnummerInTest = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(1),
            LocalDate.now()
        )

        private fun kostraRecordInTest(journalId: String = "~journalId~") = KostraRecord(
            1,
            mapOf(
                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to journalId,
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to fodselsnummerInTest
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associateBy { it.name }
        )
    }
}
