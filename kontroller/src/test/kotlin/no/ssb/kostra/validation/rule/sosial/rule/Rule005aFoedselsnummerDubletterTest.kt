package no.ssb.kostra.validation.rule.sosial.rule

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import java.time.LocalDate

class Rule005aFoedselsnummerDubletterTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule005aFoedselsnummerDubletter(),
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
                    "two records, different fødselsnummer",
                    listOf(
                        kostraRecordInTest(),
                        kostraRecordInTest(
                            RandomUtils.generateRandomSSN(
                                LocalDate.now().minusYears(1),
                                LocalDate.now()
                            )
                        )
                    ),
                ),
                ForAllRowItem(
                    "two records with same fødselsnummer",
                    listOf(
                        kostraRecordInTest(),
                        kostraRecordInTest(journalId = "~journalId2~")
                    ),
                    expectedErrorMessage = "Fødselsnummeret i journalnummer ~journalId~ fins også i journalene ~journalId2~.",
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

        private fun kostraRecordInTest(
            foedselsnummer: String = fodselsnummerInTest,
            journalId: String = "~journalId~"
        ) = KostraRecord(
            1,
            mapOf(
                SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                PERSON_JOURNALNR_COL_NAME to journalId,
                PERSON_FODSELSNR_COL_NAME to foedselsnummer
            ),
            fieldDefinitions.associateBy { it.name }
        )
    }
}
