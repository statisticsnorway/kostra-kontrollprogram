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

class KommuneKostraMainTest : BehaviorSpec({
    Given("KommuneKostraMain") {
        forAll(
            *regions.flatMap { region ->
                validSkjemaTypes.map { skjema ->
                    row(
                        "skjema = $skjema, region = $region -> validating an empty record string",
                        KotlinArguments(
                            skjema = skjema,
                            aargang = RuleTestData.argumentsInTest.aargang,
                            region = region,
                            inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength)
                        ),
                        NUMBER_OF_VALIDATIONS,
                        7
                    )
                }
            }.toTypedArray(),
            *regions.flatMap { region ->
                validSkjemaTypes.map { skjema ->
                    row(
                        "skjema = $skjema, region = $region -> validating an invalid record string",
                        KotlinArguments(
                            skjema = skjema,
                            aargang = RuleTestData.argumentsInTest.aargang,
                            region = region,
                            inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength + 10)
                        ),
                        1,
                        1
                    )
                }
            }.toTypedArray(),
            *regions.flatMap { region ->
                mapOf(
                    // "0A" to 0, // FIX ME
                    "0B" to 3,
                    //"0C" to 1, // FIX ME
                    "0D" to 3,
                    "0I" to 1,
                    "0J" to 3,
                    "0K" to 1,
                    "0L" to 3,
                    "0M" to 2,
                    "0N" to 3,
                    "0P" to 2,
                    "0Q" to 3
                ).map { (skjema, expectedNumberOfControls) ->
                    row(
                        "skjema = $skjema, region = $region -> validating a valid record string",
                        argumentsInTest(
                            argumentsSkjema = skjema,
                            recordSkjema = skjema,
                            argumentsRegion = region,
                            recordRegion = region
                        ),
                        NUMBER_OF_VALIDATIONS,
                        expectedNumberOfControls
                    )
                }
            }.toTypedArray(),
/* FIX ME
            row(
                "skjema = 0A, region = 123400 -> validating a valid record string with invalid data",
                argumentsInTest(
                    argumentsSkjema = "0A",
                    recordSkjema = "0A",
                    argumentsRegion = "123400",
                    recordRegion = "123400",
                    recordVersion = "XXXX"
                ),
                NUMBER_OF_VALIDATIONS,
                1
            )
*/
        ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = KommuneKostraMain(kotlinArguments).validate()

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
        private val regions = listOf("123400", "030100", "040000")
        private val validSkjemaTypes = listOf("0A", "0B", "0C", "0D", "0I", "0J", "0K", "0L", "0M", "0N", "0P", "0Q")
        private const val NUMBER_OF_VALIDATIONS = 51

        private fun argumentsInTest(
            argumentsVersion: String = RuleTestData.argumentsInTest.aargang,
            argumentsSkjema: String = validSkjemaTypes.first(),
            argumentsRegion: String = RuleTestData.argumentsInTest.region,
            argumentsOrgnr: String =
                if (argumentsSkjema in listOf("0I", "0J", "0K", "0L")) "958935420" else " ".repeat(9),
            recordVersion: String = argumentsVersion,
            recordSkjema: String = argumentsSkjema,
            recordRegion: String = argumentsRegion,
            recordOrgnr: String = argumentsOrgnr
        ): KotlinArguments = KotlinArguments(
            skjema = argumentsSkjema,
            aargang = argumentsVersion,
            region = argumentsRegion,
            orgnr = argumentsOrgnr,
            inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to recordSkjema,
                        RegnskapConstants.FIELD_AARGANG to recordVersion,
                        RegnskapConstants.FIELD_REGION to recordRegion,
                        RegnskapConstants.FIELD_ORGNR to recordOrgnr,
                        RegnskapConstants.FIELD_KONTOKLASSE to "4",
                        RegnskapConstants.FIELD_FUNKSJON to "041 ",
                        RegnskapConstants.FIELD_ART to "200",
                        RegnskapConstants.FIELD_BELOP to "1"
                    )
                ).toRecordString()
        )
    }
}