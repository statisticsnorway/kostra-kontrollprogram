package no.ssb.kostra.area.famvern.famvern52b

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aColumnNames
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aFieldDefinitions
import no.ssb.kostra.area.famvern.famvern52a.Familievern52aMain
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.byColumnName
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.program.extension.toRecordString
import no.ssb.kostra.validation.rule.RuleTestData

class Familievern52bMainTest : BehaviorSpec({
    Given("Familievern52bMain") {
        forAll(
            row(
                "validating an invalid record string",
                KotlinArguments(
                    skjema = skjema,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = region,
                    inputFileContent = " ".repeat(Familievern52bFieldDefinitions.fieldLength + 10)
                ),
                1,
                1
            ),
            row(
                "validating an empty record string",
                KotlinArguments(
                    skjema = skjema,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = region,
                    inputFileContent = " ".repeat(Familievern52bFieldDefinitions.fieldLength)
                ),
                numberOfValidations,
                17
            ),
            row(
                "validating a valid record string",
                argumentsInTest(),
                numberOfValidations,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                argumentsInTest(region = "XXXXXX"),
                numberOfValidations,
                2
            )

        ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = Familievern52bMain(kotlinArguments).validate()

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
        private const val skjema = "52BF"
        private const val region = "667600"
        private const val numberOfValidations = 20

        private fun argumentsInTest(
            region: String = "667600",

            ): KotlinArguments = KotlinArguments(
            skjema = skjema,
            aargang = RuleTestData.argumentsInTest.aargang,
            region = region,
            inputFileContent = " ".repeat(Familievern52bFieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, Familievern52bFieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        Familievern52bColumnNames.REGION_NR_B_COL_NAME to region,
                        Familievern52bColumnNames.KONTOR_NR_B_COL_NAME to "017",
                        Familievern52bColumnNames.GRUPPE_NR_B_COL_NAME to "~grp~",
                        Familievern52bColumnNames.GRUPPE_NAVN_B_COL_NAME to "gruppenavn",
                        Familievern52bColumnNames.DATO_GRSTART_B_COL_NAME to "0101"+RuleTestData.argumentsInTest.aargang,
                        Familievern52bColumnNames.STRUKTUR_GR_B_COL_NAME to Familievern52bFieldDefinitions.fieldDefinitions.byColumnName(Familievern52bColumnNames.STRUKTUR_GR_B_COL_NAME).codeList.first().code,
                        Familievern52bColumnNames.HOVEDI_GR_B_COL_NAME to Familievern52bFieldDefinitions.fieldDefinitions.byColumnName(Familievern52bColumnNames.HOVEDI_GR_B_COL_NAME).codeList.first().code,
                        Familievern52bColumnNames.ANTMOTERTOT_IARET_B_COL_NAME to "1",
                        Familievern52bColumnNames.ANTMOTERTOT_OPPR_B_COL_NAME to "1",
                        Familievern52bColumnNames.TIMERTOT_IARET_B_COL_NAME to "1",
                        Familievern52bColumnNames.TIMERTOT_OPPR_B_COL_NAME to "1",
                        Familievern52bColumnNames.ANTDELT_IARET_B_COL_NAME to "1",
                        Familievern52bColumnNames.ANTDELT_OPPR_B_COL_NAME to "1",
                        Familievern52bColumnNames.ANTTER_GRUPPEB_B_COL_NAME to "1",
                        Familievern52bColumnNames.TOLK_B_COL_NAME to Familievern52bFieldDefinitions.fieldDefinitions.byColumnName(Familievern52bColumnNames.TOLK_B_COL_NAME).codeList.first().code,
                        Familievern52bColumnNames.STATUS_ARETSSL_B_COL_NAME to Familievern52bFieldDefinitions.fieldDefinitions.byColumnName(Familievern52bColumnNames.STATUS_ARETSSL_B_COL_NAME).codeList.first().code,
                        Familievern52bColumnNames.DATO_GRAVSLUTN_B_COL_NAME to "0202"+RuleTestData.argumentsInTest.aargang,
                    )
                )
                .toRecordString()
        )
    }
}