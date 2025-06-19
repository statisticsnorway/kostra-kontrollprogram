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

class KommuneKostraMainTest :
    BehaviorSpec({
        Given("KommuneKostraMain") {
            forAll(
                *regions
                    .flatMap { region ->
                        mapOf(
                            "0A" to 5,
                            "0B" to 5,
                            "0C" to 5,
                            "0D" to 5,
                            "0I" to 5,
                            "0J" to 5,
                            "0K" to 5,
                            "0L" to 5,
                            "0M" to 5,
                            "0N" to 5,
                            "0P" to 5,
                            "0Q" to 5,
                        ).map { (skjema, expectedNumberOfControls) ->
                            row(
                                "validating an empty record string -> skjema = $skjema, region = $region",
                                KotlinArguments(
                                    skjema = skjema,
                                    aargang = RuleTestData.argumentsInTest.aargang,
                                    region = region,
                                    inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength),
                                ),
                                NUMBER_OF_VALIDATIONS,
                                expectedNumberOfControls,
                            )
                        }.plus(
                            validSkjemaTypes.map { skjema ->
                                row(
                                    "validating an invalid record string -> skjema = $skjema, region = $region",
                                    KotlinArguments(
                                        skjema = skjema,
                                        aargang = RuleTestData.argumentsInTest.aargang,
                                        region = region,
                                        inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength + 10),
                                    ),
                                    1,
                                    1,
                                )
                            },
                        ).plus(
                            mapOf(
                                "0A" to 2,
                                "0B" to 4,
                                "0C" to 2,
                                "0D" to 4,
                                "0I" to 1,
                                "0J" to 4,
                                "0K" to 1,
                                "0L" to 4,
                                "0M" to 2,
                                "0N" to 4,
                                "0P" to 2,
                                "0Q" to 4,
                            ).map { (skjema, expectedNumberOfControls) ->
                                row(
                                    "validating a valid record string -> skjema = $skjema, region = $region",
                                    argumentsInTest(
                                        argumentsSkjema = skjema,
                                        recordSkjema = skjema,
                                        argumentsRegion = region,
                                        recordRegion = region,
                                    ),
                                    NUMBER_OF_VALIDATIONS,
                                    expectedNumberOfControls,
                                )
                            },
                        )
                    }.toTypedArray(),
                row(
                    "validating a valid record string with invalid data -> skjema = 0A, region = 123400",
                    argumentsInTest(
                        argumentsSkjema = "0A",
                        recordSkjema = "0A",
                        argumentsRegion = "123400",
                        recordRegion = "123400",
                        recordVersion = "XXXX",
                    ),
                    NUMBER_OF_VALIDATIONS,
                    3,
                ),
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
        private const val NUMBER_OF_VALIDATIONS = 53

        private fun argumentsInTest(
            argumentsVersion: String = RuleTestData.argumentsInTest.aargang,
            argumentsSkjema: String = validSkjemaTypes.first(),
            argumentsRegion: String = RuleTestData.argumentsInTest.region,
            argumentsOrgnr: String =
                if (argumentsSkjema in listOf("0I", "0J", "0K", "0L")) "958935420" else " ".repeat(9),
            recordVersion: String = argumentsVersion,
            recordSkjema: String = argumentsSkjema,
            recordRegion: String = argumentsRegion,
            recordOrgnr: String = argumentsOrgnr,
        ): KotlinArguments =
            KotlinArguments(
                skjema = argumentsSkjema,
                aargang = argumentsVersion,
                region = argumentsRegion,
                orgnr = argumentsOrgnr,
                inputFileContent =
                    " "
                        .repeat(RegnskapFieldDefinitions.fieldDefinitions.last().to)
                        .toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
                        .plus(
                            mapOf(
                                RegnskapConstants.FIELD_SKJEMA to recordSkjema,
                                RegnskapConstants.FIELD_AARGANG to recordVersion,
                                RegnskapConstants.FIELD_REGION to recordRegion,
                                RegnskapConstants.FIELD_ORGNR to recordOrgnr,
                                RegnskapConstants.FIELD_KONTOKLASSE to if (argumentsSkjema in listOf("0A", "0C")) "0" else "4",
                                RegnskapConstants.FIELD_FUNKSJON to "041 ",
                                RegnskapConstants.FIELD_ART to "200",
                                RegnskapConstants.FIELD_BELOP to "1",
                            ),
                        ).toRecordString(),
            )
    }
}
