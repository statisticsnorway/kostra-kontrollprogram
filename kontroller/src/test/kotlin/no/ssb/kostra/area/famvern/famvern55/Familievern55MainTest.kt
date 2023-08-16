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
                    skjema = skjema,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = region,
                    inputFileContent = " ".repeat(Familievern55FieldDefinitions.fieldLength + 10)
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
                    inputFileContent = " ".repeat(Familievern55FieldDefinitions.fieldLength)
                ),
                numberOfValidations,
                1
            ),
            row(
                "validating a valid record string",
                argumentsInTest("30"),
                numberOfValidations,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                argumentsInTest(fylke = "XX"),
                numberOfValidations,
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
        private const val skjema = "55F"
        private const val region = "667600"
        private const val numberOfValidations = 14

        private fun argumentsInTest(
            fylke: String = "30",

            ): KotlinArguments = KotlinArguments(
            skjema = skjema,
            aargang = RuleTestData.argumentsInTest.aargang,
            region = region,
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