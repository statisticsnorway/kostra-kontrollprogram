package no.ssb.kostra.area.regnskap.kostra

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_AARGANG
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KotlinArguments
import no.ssb.kostra.program.extension.plus
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.program.extension.toRecordString
import no.ssb.kostra.validation.rule.RuleTestData
import java.time.Year

class KirkeKostraMainTest : BehaviorSpec({
    Given("KirkeKostraMain") {
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
                30,
                3
            ),
            row(
                "validating a valid record string",
                recordStringInTest("2022"),
                30,
                0
            ),
            row(
                "validating a valid record string with invalid data",
                recordStringInTest("XXXX"),
                30,
                1
            ),
        ) { description, inputFileContent, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = KirkeKostraMain(
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
                        FIELD_SKJEMA to "0A",
                        FIELD_AARGANG to version,
                        FIELD_REGION to "1234  ",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "200",
                        FIELD_BELOP to "1"
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