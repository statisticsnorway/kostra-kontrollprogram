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
                    skjema = SKJEMA,
                    aargang = RuleTestData.argumentsInTest.aargang,
                    region = REGION,
                    inputFileContent = " ".repeat(Familievern53FieldDefinitions.fieldLength + 10)
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
                    inputFileContent = " ".repeat(Familievern53FieldDefinitions.fieldLength)
                ),
                NUMBER_OF_VALIDATIONS,
                10
            ),
            row(
                "validating a valid record string",
                argumentsInTest(fylke = "03"),
                NUMBER_OF_VALIDATIONS,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                argumentsInTest(fylke = "XX"),
                NUMBER_OF_VALIDATIONS,
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
        private const val SKJEMA = "53F"
        private const val REGION = "667600"
        private const val NUMBER_OF_VALIDATIONS = 7

        private fun argumentsInTest(fylke: String): KotlinArguments = KotlinArguments(
            skjema = SKJEMA,
            aargang = RuleTestData.argumentsInTest.aargang,
            region = REGION,
            inputFileContent = " ".repeat(Familievern53FieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, Familievern53FieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        Familievern53ColumnNames.FYLKE_NR_COL_NAME to fylke,
                        Familievern53ColumnNames.KONTORNR_COL_NAME to "038",
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