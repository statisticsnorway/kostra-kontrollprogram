package no.ssb.kostra.area.regnskap.kostra

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.program.extension.toRecordString
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.Year

class KommuneKostraMainTest : BehaviorSpec({
    Given("KommuneKostraMain") {
        forAll(
            row(
                "validating an invalid record string",
                " ".repeat(RegnskapFieldDefinitions.fieldLength + 10),
                1,
                1
            ),
            row(
                "validating an empty record string",
                " ".repeat(RegnskapFieldDefinitions.fieldLength),
                51,
                3
            ),
            row(
                "validating a valid record string",
                recordStringInTest("2022"),
                51,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                recordStringInTest("XXXX"),
                51,
                1
            ),
        ) { description, inputFileContent, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = KommuneKostraMain(
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
        private fun recordStringInTest(version: String) =
            " ".repeat(RegnskapFieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to "0A",
                        RegnskapConstants.FIELD_AARGANG to version,
                        RegnskapConstants.FIELD_REGION to "1234  ",
                        RegnskapConstants.FIELD_KONTOKLASSE to "4",
                        RegnskapConstants.FIELD_FUNKSJON to "041 ",
                        RegnskapConstants.FIELD_ART to "200",
                        RegnskapConstants.FIELD_BELOP to "1"
                    )
                )
                .toRecordString()

        private fun argumentsInTest(
            inputFileContent: String
        ): KotlinArguments = KotlinArguments(
            skjema = "0A",
            aargang = (Year.now().value - 1).toString(),
            region = RuleTestData.argumentsInTest.region,

            inputFileContent = inputFileContent
        )
    }
}