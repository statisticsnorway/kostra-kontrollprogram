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

class KirkeKostraMainTest :
    BehaviorSpec({
        Given("KirkeKostraMain") {
            forAll(
                *validSkjemaTyper
                    .map { skjema ->
                        row(
                            "skjema $skjema -> validating an invalid record string",
                            KotlinArguments(
                                skjema = skjema,
                                aargang = "2022",
                                region = "1234  ",
                                inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength + 10),
                            ),
                            1,
                            1,
                        )
                    }.toTypedArray(),
                *setOf(
                    "0F" to 5,
                    "0G" to 5,
                ).map { (skjema, expectedNumberOfControls) ->
                    row(
                        "skjema $skjema -> validating an empty record string",
                        KotlinArguments(
                            skjema = skjema,
                            aargang = "2022",
                            region = "1234  ",
                            inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength),
                        ),
                        NUMBER_OF_VALIDATIONS,
                        expectedNumberOfControls,
                    )
                }.toTypedArray(),
                *setOf(
                    "0F" to 0,
                    "0G" to 5,
                ).map { (skjema, expectedNumberOfControls) ->
                    row(
                        "skjema $skjema -> validating a valid record string",
                        argumentsInTest(argumentsSkjema = skjema, recordSkjema = skjema),
                        NUMBER_OF_VALIDATIONS,
                        expectedNumberOfControls,
                    )
                }.toTypedArray(),
                *validSkjemaTyper
                    .map { skjema ->
                        row(
                            "skjema $skjema -> validating a valid record string with invalid data",
                            argumentsInTest(recordVersion = "XXXX"),
                            NUMBER_OF_VALIDATIONS,
                            1,
                        )
                    }.toTypedArray(),
            ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
                When(description) {
                    val validationResult = KirkeKostraMain(kotlinArguments).validate()

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
        private val validSkjemaTyper = setOf("0F", "0G")
        private const val NUMBER_OF_VALIDATIONS = 30

        private fun argumentsInTest(
            argumentsVersion: String = RuleTestData.argumentsInTest.aargang,
            argumentsSkjema: String = validSkjemaTyper.first(),
            argumentsRegion: String = RuleTestData.argumentsInTest.region,
            argumentsOrgnr: String =
                if (argumentsSkjema in listOf("0I", "0J", "0K", "0L")) {
                    "987654321"
                } else {
                    " ".repeat(
                        9,
                    )
                },
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
                                RegnskapConstants.FIELD_KONTOKLASSE to "4",
                                RegnskapConstants.FIELD_FUNKSJON to "041 ",
                                RegnskapConstants.FIELD_ART to "200",
                                RegnskapConstants.FIELD_BELOP to "1",
                            ),
                        ).toRecordString(),
            )
    }
}
