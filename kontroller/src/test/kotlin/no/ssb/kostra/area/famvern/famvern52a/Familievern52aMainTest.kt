package no.ssb.kostra.area.famvern.famvern52a

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.program.extension.toRecordString
import no.ssb.kostra.validation.rule.RuleTestData

class Familievern52aMainTest : BehaviorSpec({
    Given("Familievern52aMain") {
        forAll(
            row(
                "validating an invalid record string",
                KotlinArguments(
                    skjema = SKJEMA,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = REGION,
                    inputFileContent = " ".repeat(Familievern52aFieldDefinitions.fieldLength + 10)
                ),
                1,
                1
            ),
            row(
                "validating an empty record string",
                KotlinArguments(
                    skjema = SKJEMA,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = REGION,
                    inputFileContent = " ".repeat(Familievern52aFieldDefinitions.fieldLength)
                ),
                36,
                42
            ),
            row(
                "validating a valid record string",
                argumentsInTest(region = "667600"),
                NUMBER_OF_VALIDATIONS,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                argumentsInTest(region = "XXXXXX"),
                NUMBER_OF_VALIDATIONS,
                2
            )
        ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = Familievern52aMain(kotlinArguments).validate()

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
        private const val SKJEMA = "52AF"
        private const val REGION = "667600"
        private const val NUMBER_OF_VALIDATIONS = 36

        private fun argumentsInTest(region: String): KotlinArguments = KotlinArguments(
            skjema = SKJEMA,
            aargang = RuleTestData.argumentsInTest.aargang,
            region = region,
            inputFileContent = " ".repeat(Familievern52aFieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, Familievern52aFieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        Familievern52aColumnNames.REGION_NR_A_COL_NAME to region,
                        Familievern52aColumnNames.KONTOR_NR_A_COL_NAME to "017",
                        Familievern52aColumnNames.JOURNAL_NR_A_COL_NAME to "~journal~",
                        Familievern52aColumnNames.HENV_DATO_A_COL_NAME to "0101" + RuleTestData.argumentsInTest.aargang,
                        Familievern52aColumnNames.KONTAKT_TIDL_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.KONTAKT_TIDL_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.HENV_GRUNN_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.HENV_GRUNN_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.PRIMK_KJONN_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.PRIMK_KJONN_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.PRIMK_FODT_A_COL_NAME to (RuleTestData.argumentsInTest.aargang.toInt() - 40).toString(),
                        Familievern52aColumnNames.PRIMK_SIVILS_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.PRIMK_SIVILS_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.PRIMK_SAMBO_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.PRIMK_SAMBO_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.PRIMK_ARBSIT_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.PRIMK_ARBSIT_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.FORSTE_SAMT_A_COL_NAME to "0202" + RuleTestData.argumentsInTest.aargang,
                        Familievern52aColumnNames.TEMA_PARREL_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.TEMA_PARREL_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.HOVEDF_BEHAND_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.HOVEDF_BEHAND_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.DELT_PARTNER_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.DELT_PARTNER_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.SAMT_PRIMK_A_COL_NAME to "1",
                        Familievern52aColumnNames.SAMT_PARTNER_A_COL_NAME to "1",
                        Familievern52aColumnNames.ANTSAMT_HOVEDT_A_COL_NAME to "1",
                        Familievern52aColumnNames.ANTSAMT_IARET_A_COL_NAME to "1",
                        Familievern52aColumnNames.ANTSAMT_OPPR_A_COL_NAME to "1",
                        Familievern52aColumnNames.TIMER_IARET_A_COL_NAME to "1",
                        Familievern52aColumnNames.TIMER_OPPR_A_COL_NAME to "1",
                        Familievern52aColumnNames.SAMARB_INGEN_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.SAMARB_INGEN_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.STATUS_ARETSSL_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.HOVEDTEMA_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.HOVEDTEMA_A_COL_NAME
                        ).codeList.first().code,
                        Familievern52aColumnNames.DATO_AVSL_A_COL_NAME to "0303" + RuleTestData.argumentsInTest.aargang,
                        Familievern52aColumnNames.BEKYMR_MELD_A_COL_NAME to Familievern52aFieldDefinitions.fieldDefinitions.byColumnName(
                            Familievern52aColumnNames.BEKYMR_MELD_A_COL_NAME
                        ).codeList.first().code,
                    )
                ).toRecordString()
        )
    }
}