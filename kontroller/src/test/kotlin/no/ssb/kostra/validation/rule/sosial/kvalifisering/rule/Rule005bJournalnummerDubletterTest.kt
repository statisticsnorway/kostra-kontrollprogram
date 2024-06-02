package no.ssb.kostra.validation.rule.sosial.kvalifisering.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.SharedConstants.OSLO_MUNICIPALITY_ID
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
        KostraTestFactory.validationRuleNoContextTest(
            sut = Rule005bJournalnummerDubletter(),
            expectedSeverity = Severity.ERROR,
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
                "two records for Oslo with same journalId",
                listOf(
                    kostraRecordInTest(kommunenr = OSLO_MUNICIPALITY_ID),
                    kostraRecordInTest(kommunenr = OSLO_MUNICIPALITY_ID)
                )
            ),
            ForAllRowItem(
                "two records with same journalId",
                listOf(
                    kostraRecordInTest(),
                    kostraRecordInTest()
                ),
                expectedErrorMessage = "Journalnummer ~journalId~ forekommer 2 ganger.",
                expectedSize = 2
            )
        )
    )

}) {
    companion object {
        private val fodselsnummerInTest = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(1),
            LocalDate.now()
        )

        private fun kostraRecordInTest(
            kommunenr: String = RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
            journalId: String = "~journalId~"
        ) = KostraRecord(
            1,
            mapOf(
                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME to kommunenr,
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to journalId,
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to fodselsnummerInTest
            ),
            KvalifiseringFieldDefinitions.fieldDefinitions.associateBy { it.name }
        )
    }
}
