package no.ssb.kostra.area.famvern.famvern53

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

class Familievern53MainTest : BehaviorSpec({
    Given("Familievern53Main") {
        forAll(
            row(
                "validating an invalid record string",
                KotlinArguments(
                    skjema = skjema,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = region,
                    inputFileContent = " ".repeat(Familievern53FieldDefinitions.fieldLength + 10)
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
                    inputFileContent = " ".repeat(Familievern53FieldDefinitions.fieldLength)
                ),
                numberOfValidations,
                10
            ),
            row(
                "validating a valid record string",
                argumentsInTest(),
                numberOfValidations,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                argumentsInTest(fylke = "XX"),
                numberOfValidations,
                2
            )
        ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = Familievern53Main(kotlinArguments).validate()

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
        private const val skjema = "53F"
        private const val region = "667600"
        private const val numberOfValidations = 6

        private fun argumentsInTest(
            fylke: String = "30",

            ): KotlinArguments = KotlinArguments(
            skjema = skjema,
            aargang = RuleTestData.argumentsInTest.aargang,
            region = region,
            inputFileContent = " ".repeat(Familievern53FieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, Familievern53FieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        Familievern53ColumnNames.FYLKE_NR_COL_NAME to fylke,
                        Familievern53ColumnNames.KONTORNR_COL_NAME to "017",
                    )
                )
                .plus(
                    Familievern53Constants.rule010Mappings.associate { it.tiltakField to "1" }
                )
                .plus(
                    Familievern53Constants.rule010Mappings.associate { it.timerField to "1" }
                )
                .toRecordString()
        )
    }
}