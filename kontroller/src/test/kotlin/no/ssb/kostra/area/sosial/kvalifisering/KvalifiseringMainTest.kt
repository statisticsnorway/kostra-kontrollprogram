package no.ssb.kostra.area.sosial.kvalifisering

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.program.extension.toRecordString
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.rule.RuleTestData
import no.ssb.kostra.validation.rule.sosial.kvalifisering.rule.Rule028MaanederMedKvalifiseringsstonad
import java.time.LocalDate
import java.time.Year

class KvalifiseringMainTest : BehaviorSpec({
    Given("KvalifiseringMain") {
        forAll(
            row(
                "validating an invalid record string",
                " ".repeat(KvalifiseringFieldDefinitions.fieldLength + 10),
                1,
                1
            ),
            row(
                "validating an empty record string",
                " ".repeat(KvalifiseringFieldDefinitions.fieldLength),
                32,
                13
            ),
            row(
                "validating a valid record string",
                recordStringInTest("22"),
                32,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                recordStringInTest("XX"),
                32,
                1
            ),
        ) { description, inputFileContent, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = KvalifiseringMain(
                    argumentsInTest(
                        inputFileContent = inputFileContent
                    )
                ).validate()

                Then("validationResult should be as expected") {
                    assertSoftly(validationResult) {
                        numberOfControls shouldBe expectedNumberOfControls
                        reportEntries.size shouldBe expectedReportEntriesSize
                    }
                }
            }
        }

    }
}) {
    companion object {
        private val fodselsnummer = RandomUtils.generateRandomSSN(
            LocalDate.now().minusYears(40),
            LocalDate.now().minusYears(21)
        )

        private fun recordStringInTest(version: String) = KostraRecord(
            1,
            mapOf(
                *((KvalifiseringFieldDefinitions.fieldDefinitionsByName).map {
                    it.key to " ".repeat(it.value.to + 1 - it.value.from)
                }).toTypedArray(),

                KvalifiseringColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                KvalifiseringColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
                KvalifiseringColumnNames.BYDELSNR_COL_NAME to "  ",
                KvalifiseringColumnNames.VERSION_COL_NAME to version,
                KvalifiseringColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalNummer~",
                KvalifiseringColumnNames.PERSON_FODSELSNR_COL_NAME to fodselsnummer,
                KvalifiseringColumnNames.KJONN_COL_NAME to "1",
                KvalifiseringColumnNames.STATUS_COL_NAME to "1",
                KvalifiseringColumnNames.EKTSTAT_COL_NAME to "1",
                KvalifiseringColumnNames.HAR_BARN_UNDER_18_COL_NAME to "1",
                KvalifiseringColumnNames.ANT_BARN_UNDER_18_COL_NAME to "1",
                KvalifiseringColumnNames.REG_DATO_COL_NAME to "010122",
                KvalifiseringColumnNames.VEDTAK_DATO_COL_NAME to "010122",
                KvalifiseringColumnNames.BEGYNT_DATO_COL_NAME to "010122",
                KvalifiseringColumnNames.AVSL_DATO_COL_NAME to " ".repeat(6),
                KvalifiseringColumnNames.KVP_KOMM_COL_NAME to "1",
                KvalifiseringColumnNames.YTELSE_SOSHJELP_COL_NAME to "1",
                KvalifiseringColumnNames.YTELSE_TYPE_SOSHJ_COL_NAME to "3",
                KvalifiseringColumnNames.KOMMNR_KVP_KOMM_COL_NAME to "1106",
                KvalifiseringColumnNames.KVP_MED_ASTONAD_COL_NAME to "1",

                KvalifiseringColumnNames.KVP_MED_KOMMBOS_COL_NAME to "1",
                KvalifiseringColumnNames.KVP_MED_HUSBANK_COL_NAME to "5",
                KvalifiseringColumnNames.KVP_MED_SOSHJ_ENGANG_COL_NAME to "9",
                KvalifiseringColumnNames.KVP_MED_SOSHJ_PGM_COL_NAME to "8",
                KvalifiseringColumnNames.KVP_MED_SOSHJ_SUP_COL_NAME to "7",

                KvalifiseringColumnNames.KVP_STONAD_COL_NAME to "42",

                *((1..12).map {
                    "${Rule028MaanederMedKvalifiseringsstonad.MONTH_PREFIX}$it" to it.toString().padStart(2, '0')
                }).toTypedArray(),
            ),
            KvalifiseringFieldDefinitions.fieldDefinitionsByName
        ).toRecordString()

        private fun argumentsInTest(
            inputFileContent: String
        ): KotlinArguments = KotlinArguments(
            aargang = (Year.now().value - 1).toString(),
            region = RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
            skjema = "11CF",

            inputFileContent = inputFileContent
        )
    }
}