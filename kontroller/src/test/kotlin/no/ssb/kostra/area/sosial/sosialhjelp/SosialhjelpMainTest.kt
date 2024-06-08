package no.ssb.kostra.area.sosial.sosialhjelp

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import no.ssb.kostra.area.sosial.SosialConstants.MONTH_PREFIX
import no.ssb.kostra.area.sosial.sosialhjelp.SosialhjelpFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.*
import no.ssb.kostra.testutil.RandomUtils
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.report.ValidationReport
import no.ssb.kostra.validation.report.ValidationReportArguments
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.LocalDate
import java.time.Year

class SosialhjelpMainTest : BehaviorSpec({
    Given("SosialhjelpMain") {
        forAll(
            row(
                "validating an invalid record string",
                true,
                " ".repeat(SosialhjelpFieldDefinitions.fieldLength + 10),
                2,
                1
            ),
            row(
                "validating an empty record string",
                true,
                " ".repeat(SosialhjelpFieldDefinitions.fieldLength),
                45,
                18
            ),
            row(
                "validating a valid record string",
                true,
                recordStringInTest("23"),
                45,
                2
            ),
            row(
                "validating a valid record string with invalid data",
                true,
                recordStringInTest("XX"),
                45,
                3
            ),
            row(
                "validating an 'empty' attachment containing only 1 space",
                false,
                " ",
                2,
                1
            ),
        ) { description, hasAttachment, inputFileContent, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = SosialhjelpMain(
                    argumentsInTest(
                        hasAttachment = hasAttachment,
                        inputFileContent = inputFileContent
                    )
                ).validate()

                val validationReport = ValidationReport(
                    validationReportArguments = ValidationReportArguments(
                        kotlinArguments = argumentsInTest(
                            hasAttachment = hasAttachment,
                            inputFileContent = inputFileContent
                        ),
                        validationResult = validationResult
                    )
                )

                val reportString = validationReport.toString()

                Then("validationResult should be as expected") {
                    assertSoftly(validationResult) {
                        numberOfControls shouldBe expectedNumberOfControls
                        reportEntries.size shouldBe expectedReportEntriesSize
                    }
                }

                Then("validationReport should be as expected") {
                    reportString shouldContain "Kontrollrapport"
                }

                if (!hasAttachment){
                    Then("validationResult.severity should be OK"){
                        validationResult.severity shouldBe Severity.OK
                    }

                    Then("reportString should contain expected text"){
                        reportString shouldContain "Det er krysset av i skjemaet at det ikke finnes deltakere " +
                                "og fil som kun inneholder et mellomrom er levert."
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

        private fun recordStringInTest(version: String) =
            " ".repeat(fieldDefinitions.last().to)
                .toKostraRecord(1, fieldDefinitions)
                .plus(
                    mapOf(
                        SosialhjelpColumnNames.SAKSBEHANDLER_COL_NAME to "Sara Saksbehandler",
                        SosialhjelpColumnNames.KOMMUNE_NR_COL_NAME to RuleTestData
                            .argumentsInTest.region.municipalityIdFromRegion(),
                        SosialhjelpColumnNames.BYDELSNR_COL_NAME to "  ",
                        SosialhjelpColumnNames.VERSION_COL_NAME to version,
                        SosialhjelpColumnNames.PERSON_JOURNALNR_COL_NAME to "~journalNummer~",
                        SosialhjelpColumnNames.PERSON_FODSELSNR_COL_NAME to fodselsnummer,
                        SosialhjelpColumnNames.KJONN_COL_NAME to "1",
                        SosialhjelpColumnNames.STATUS_COL_NAME to "1",
                        SosialhjelpColumnNames.EKTSTAT_COL_NAME to "1",
                        SosialhjelpColumnNames.HAR_BARN_UNDER_18_COL_NAME to "1",
                        SosialhjelpColumnNames.ANT_BARN_UNDER_18_COL_NAME to "1",
                        SosialhjelpColumnNames.AVSL_DATO_COL_NAME to " ".repeat(6),

                        SosialhjelpColumnNames.VKLO_COL_NAME to fieldDefinitions
                            .byColumnName(SosialhjelpColumnNames.VKLO_COL_NAME).codeList.first().code,
                        SosialhjelpColumnNames.ARBSIT_COL_NAME to fieldDefinitions
                            .byColumnName(SosialhjelpColumnNames.ARBSIT_COL_NAME).codeList.last().code,
                        SosialhjelpColumnNames.BIDRAG_COL_NAME to "1000",
                        SosialhjelpColumnNames.GITT_OKONOMIRAD_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.GITT_OKONOMIRAD_COL_NAME
                            ).codeList.first().code,
                        SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.FAAT_INDIVIDUELL_PLAN_COL_NAME
                            ).codeList.first().code,
                        SosialhjelpColumnNames.BOSIT_COL_NAME to fieldDefinitions
                            .byColumnName(SosialhjelpColumnNames.BOSIT_COL_NAME).codeList.first().code,
                        SosialhjelpColumnNames.BIDRAG_1_COL_NAME to "1000",
                        SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.VILKARSOSLOV_COL_NAME
                            ).codeList.last().code,
                        SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME to fieldDefinitions
                            .byColumnName(
                                SosialhjelpColumnNames.VILKARSAMEKT_COL_NAME
                            ).codeList.last().code,

                        *((1..12).map {
                            "$MONTH_PREFIX$it" to it.toString().padStart(2, '0')
                        }).toTypedArray(),
                    )
                )
                .toRecordString()

        private fun argumentsInTest(
            hasAttachment: Boolean,
            inputFileContent: String
        ): KotlinArguments = KotlinArguments(
            aargang = (Year.now().value - 1).toString(),
            region = RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
            skjema = "11F",
            harVedlegg = hasAttachment,
            inputFileContent = inputFileContent
        )
    }
}