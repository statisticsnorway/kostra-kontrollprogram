package no.ssb.kostra.validation.rule.sosial.kvalifisering

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.ANT_BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.AVSL_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BU18_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.BYDELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.EKTSTAT_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KJONN_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_KOMM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.KVP_STONAD_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.REG_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.STATUS_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VEDTAK_DATO_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.VERSION_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions
import no.ssb.kostra.area.sosial.kvalifisering.KvalifiseringFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.testutil.RandomUtils.generateRandomSSN
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReportEntry
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringValidator.validateKvalifisering
import no.ssb.kostra.validation.rule.sosial.kvalifisering.KvalifiseringValidator.validateKvalifiseringInternal
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Control28MaanederMedKvalifiseringsstonad.Companion.MONTH_PREFIX
import java.time.LocalDate

class KvalifiseringValidatorTest : BehaviorSpec({

    Given("validateKvalifisering") {

        When("validating an empty record string") {
            val validationResult = validateKvalifisering(
                argumentsInTest.copy(
                    inputFileContent = " ".repeat(KvalifiseringFieldDefinitions.fieldLength)
                )
            )

            Then("validationResult should be as expected") {
                validationResult shouldNotBe null

                assertSoftly(validationResult){
                    numberOfControls shouldBe 32
                    reportEntries.size shouldBe 15
                }
            }
        }
    }

    Given("validateKvalifiseringInternal") {

        forAll(
            row(
                "empty list of records",
                emptyList(),
                emptyList(),
                0
            ),
            row(
                "row with validation issues",
                listOf(kostraRecordsInTests()),
                listOf(
                    ValidationReportEntry(
                        caseworker = "Sara Saksbehandler",
                        journalId = "~journalNummer~",
                        individId = fodselsnummer,
                        severity = Severity.ERROR,
                        lineNumbers = listOf(1),
                        ruleName = KvalifiseringRuleId.MOTTATT_OKONOMISK_SOSIALHJELP_27.title,
                        messageText = "Svaralternativer for feltet \"Har deltakeren i 2022 i løpet av perioden med " +
                                "kvalifiseringsstønad mottatt økonomisk sosialhjelp, kommunal bostøtte eller " +
                                "Husbankens bostøtte?\" har ugyldige koder. Feltet er obligatorisk å fylle ut. Det er " +
                                "mottatt støtte. {KVP_MED_KOMMBOS=1, KVP_MED_HUSBANK=5, KVP_MED_SOSHJ_ENGANG=9, " +
                                "KVP_MED_SOSHJ_PGM=8, KVP_MED_SOSHJ_SUP=7}."
                    )
                ), 32
            )
        ) { description, kostraRecordsInTests, expectedResult, expectedNumberOfControls ->

            When(description) {
                val validationResult = validateKvalifiseringInternal(
                    kostraRecords = kostraRecordsInTests,
                    arguments = argumentsInTest
                )

                Then("result should be as expected") {
                    assertSoftly(validationResult) {
                        reportEntries shouldContainAll expectedResult
                        numberOfControls shouldBe expectedNumberOfControls
                    }
                }
            }
        }
    }
}) {
    companion object {

        private val fodselsnummer = generateRandomSSN(
            LocalDate.now().minusYears(1),
            LocalDate.now()
        )

        private fun kostraRecordsInTests() = KostraRecord(
            1,
            mapOf(
                SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KOMMUNE_NR_COL_NAME to argumentsInTest.region.municipalityIdFromRegion(),
                BYDELSNR_COL_NAME to "14",
                VERSION_COL_NAME to "22",
                PERSON_JOURNALNR_COL_NAME to "~journalNummer~",
                PERSON_FODSELSNR_COL_NAME to fodselsnummer,
                KJONN_COL_NAME to "1",
                STATUS_COL_NAME to "1",
                EKTSTAT_COL_NAME to "1",
                BU18_COL_NAME to "1",
                ANT_BU18_COL_NAME to "1",
                REG_DATO_COL_NAME to "010122",
                VEDTAK_DATO_COL_NAME to "010122",
                BEGYNT_DATO_COL_NAME to "010122",
                AVSL_DATO_COL_NAME to " ".repeat(6),
                KVP_KOMM_COL_NAME to "1",
                YTELSE_SOSHJELP_COL_NAME to "1",
                YTELSE_TYPE_SOSHJ_COL_NAME to "3",
                KOMMNR_KVP_KOMM_COL_NAME to "1106",
                KVP_MED_ASTONAD_COL_NAME to "1",

                KVP_MED_KOMMBOS_COL_NAME to "1",
                KVP_MED_HUSBANK_COL_NAME to "5",
                KVP_MED_SOSHJ_ENGANG_COL_NAME to "9",
                KVP_MED_SOSHJ_PGM_COL_NAME to "8",
                KVP_MED_SOSHJ_SUP_COL_NAME to "7",

                KVP_STONAD_COL_NAME to "42",

                *((1..12).map {
                    "${MONTH_PREFIX}$it" to it.toString().padStart(2, '0')
                }).toTypedArray()
            ),
            fieldDefinitions.associate { with(it) { name to it } }
        )
    }
}
