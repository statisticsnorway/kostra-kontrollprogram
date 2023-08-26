package no.ssb.kostra.area.famvern.famvern55

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.program.extension.toRecordString
import no.ssb.kostra.validation.rule.RuleTestData

class Familievern55MainTest : BehaviorSpec({
    Given("Familievern55Main") {
        forAll(
            row(
                "validating an invalid record string",
                KotlinArguments(
                    skjema = SKJEMA,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = REGION,
                    inputFileContent = " ".repeat(Familievern55FieldDefinitions.fieldLength + 10)
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
                    inputFileContent = " ".repeat(Familievern55FieldDefinitions.fieldLength)
                ),
                NUMBER_OF_VALIDATIONS,
                1
            ),
            row(
                "validating a valid record string",
                argumentsInTest("30"),
                NUMBER_OF_VALIDATIONS,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                argumentsInTest(fylke = "XX"),
                NUMBER_OF_VALIDATIONS,
                1
            )
        ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = Familievern55Main(kotlinArguments).validate()

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
        private const val SKJEMA = "55F"
        private const val REGION = "667600"
        private const val NUMBER_OF_VALIDATIONS = 14

        private fun argumentsInTest(
            fylke: String = "30",

            ): KotlinArguments = KotlinArguments(
            skjema = SKJEMA,
            aargang = RuleTestData.argumentsInTest.aargang,
            region = REGION,
            inputFileContent = " ".repeat(Familievern55FieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, Familievern55FieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        Familievern55ColumnNames.FYLKE_NR_COL_NAME to fylke,
                        Familievern55ColumnNames.FYLKE_NAVN_COL_NAME to "Testvik",
                    )
                )
                .toRecordString()
        )
    }
}