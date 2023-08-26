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

class KvartalKostraMainTest : BehaviorSpec({
    Given("KvartalKostraMain") {
        forAll(
            *validSkjema.map { skjema ->
                regions.map { region ->
                    listOf(
                        row(
                            "skjema = $skjema, region = $region -> validating an empty record string",
                            KotlinArguments(
                                skjema = skjema,
                                aargang = RuleTestData.argumentsInTest.aargang,
                                region = region,
                                inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldLength)
                            ),
                            NUMBER_OF_VALIDATIONS,
                            3
                        ),
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
                        ),
                        row(
                            "skjema = $skjema, region = $region -> validating a valid record string",
                            argumentsInTest(
                                argumentsSkjema = skjema,
                                recordSkjema = skjema,
                                argumentsRegion = region,
                                recordRegion = region
                            ),
                            NUMBER_OF_VALIDATIONS,
                            0
                        ),
                        row(
                            "skjema = $skjema, region = $region -> validating a valid record string with invalid data",
                            argumentsInTest(
                                argumentsSkjema = skjema,
                                recordSkjema = skjema,
                                argumentsRegion = region,
                                recordRegion = region,
                                recordVersion = "XXXX"
                            ),
                            NUMBER_OF_VALIDATIONS,
                            1
                        )
                    )
                }.flatten()
            }.flatten().toTypedArray(),
        ) { description, kotlinArguments, expectedNumberOfControls, expectedReportEntriesSize ->
            When(description) {
                val validationResult = KvartalKostraMain(kotlinArguments).validate()

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
        private val validSkjema = listOf("0AK1", "0BK1", "0CK1", "0DK1")
        private const val NUMBER_OF_VALIDATIONS = 17

        private fun argumentsInTest(
            argumentsVersion: String = RuleTestData.argumentsInTest.aargang,
            argumentsSkjema: String = validSkjema.first(),
            argumentsRegion: String = RuleTestData.argumentsInTest.region,
            argumentsKvartal: String = "1",
            recordVersion: String = argumentsVersion,
            recordSkjema: String = argumentsSkjema.substring(0, 2),
            recordRegion: String = argumentsRegion,
            recordKvartal: String = argumentsKvartal,

            ): KotlinArguments = KotlinArguments(
            skjema = argumentsSkjema,
            aargang = argumentsVersion,
            region = argumentsRegion,
            kvartal = argumentsKvartal,
            inputFileContent = " ".repeat(RegnskapFieldDefinitions.fieldDefinitions.last().to)
                .toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions)
                .plus(
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to recordSkjema,
                        RegnskapConstants.FIELD_AARGANG to recordVersion,
                        RegnskapConstants.FIELD_REGION to recordRegion,
                        RegnskapConstants.FIELD_KVARTAL to recordKvartal,
                        RegnskapConstants.FIELD_KONTOKLASSE to "0",
                        RegnskapConstants.FIELD_FUNKSJON to "100 ",
                        RegnskapConstants.FIELD_ART to "200",
                        RegnskapConstants.FIELD_BELOP to "1"
                    )
                )
                .toRecordString()
        )
    }
}