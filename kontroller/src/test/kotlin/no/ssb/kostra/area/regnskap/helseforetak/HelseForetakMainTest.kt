package no.ssb.kostra.area.regnskap.helseforetak

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.municipalityIdFromRegion
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.Year

class HelseForetakMainTest : BehaviorSpec({
    Given("KvalifiseringMain") {
        forAll(
            *setOf("0X", "0Y").map { skjema ->
                row(
                    "For $skjema, validating an invalid record string",
                    skjema,
                    " ".repeat(RegnskapFieldDefinitions.fieldLength + 10),
                    1,
                    1
                )
            }.toTypedArray(),

            *setOf(
                "0X" to 6,
                "0Y" to 4,
            ).map { (skjema, expectedNumberOfControls) ->
                row(
                    "For $skjema, validating an empty record string",
                    skjema,
                    " ".repeat(RegnskapFieldDefinitions.fieldLength),
                    19,
                    expectedNumberOfControls
                )
            }.toTypedArray(),
//            row(
//                "validating a valid record string",
//                recordStringInTest("22"),
//                32,
//                0
//            ),
//            row(
//                "validating a valid record string with invalid data",
//                recordStringInTest("XX"),
//                32,
//                1
//            ),
        ) { description, skjema, inputFileContent, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = HelseForetakMain(
                    argumentsInTest(
                        skjema = skjema,
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

        private fun argumentsInTest(
            skjema: String,
            inputFileContent: String
        ): KotlinArguments = KotlinArguments(
            aargang = (Year.now().value - 1).toString(),
            region = RuleTestData.argumentsInTest.region.municipalityIdFromRegion(),
            skjema = skjema,
            inputFileContent = inputFileContent
        )
    }
}
