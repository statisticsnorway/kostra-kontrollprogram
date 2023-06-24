package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule025KombinasjonDriftKontoklasseArtTest : BehaviorSpec({

    Given("context") {
        val invalidDriftArtList =
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "921", "922", "929", "970")
        val sut = Rule025KombinasjonDriftKontoklasseArt(invalidDriftArtList)

        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("0A", "420400", "         ", "1", "010", "1", false),
            row("0A", "420400", "         ", "1", "280", "1", true),
            row("0A", "420400", "         ", "1", "921", "1", true),
            row("0C", "420400", "         ", "1", "010", "1", false),
            row("0C", "420400", "         ", "1", "280", "1", true),
            row("0C", "420400", "         ", "1", "921", "1", true),
            row("0I", "420400", "999999999", "3", "010", "1", false),
            row("0I", "420400", "999999999", "3", "280", "1", true),
            row("0I", "420400", "999999999", "3", "921", "1", true),
            row("0I", "030101", "958935420", "3", "010", "1", false),
            row("0I", "030101", "958935420", "3", "280", "1", true),
            row("0I", "030101", "958935420", "3", "921", "1", true),
            row("0K", "420400", "999999999", "3", "010", "1", false),
            row("0K", "420400", "999999999", "3", "280", "1", true),
            row("0K", "420400", "999999999", "3", "921", "1", true),
            row("0K", "030101", "958935420", "3", "010", "1", false),
            row("0K", "030101", "958935420", "3", "280", "1", true),
            row("0K", "030101", "958935420", "3", "921", "1", true),
            row("0M", "420400", "999999999", "3", "010", "1", false),
            row("0M", "420400", "999999999", "3", "280", "1", true),
            row("0M", "420400", "999999999", "3", "921", "1", true),
            row("0M", "030101", "958935420", "3", "010", "1", false),
            row("0M", "030101", "958935420", "3", "280", "1", true),
            row("0M", "030101", "958935420", "3", "921", "1", true),
            row("0P", "420400", "999999999", "3", "010", "1", false),
            row("0P", "420400", "999999999", "3", "280", "1", true),
            row("0P", "420400", "999999999", "3", "921", "1", true),
            row("0P", "030101", "958935420", "3", "010", "1", false),
            row("0P", "030101", "958935420", "3", "280", "1", true),
            row("0P", "030101", "958935420", "3", "921", "1", true)
        ) { skjema, region, orgnr, kontoklasse, art, belop, expectedResult ->
            When("For $skjema, $region, $orgnr, $kontoklasse, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_REGION to region,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                            RegnskapConstants.FIELD_ART to art,
                            RegnskapConstants.FIELD_BELOP to belop,
                        )
                    )
                )

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér ugyldig art '${art}' " +
                                    "i driftsregnskapet til en gyldig art i driftsregnskapet eller overfør posteringen " +
                                    "til investeringsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }

    Given("context lånefond") {
        val invalidLanefondDriftArtList =
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "922", "929", "970")
        val sut = Rule025KombinasjonDriftKontoklasseArt(invalidLanefondDriftArtList)

        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("0A", "420400", "         ", "1", "921", true),
            row("0C", "420400", "         ", "1", "921", true),
            row("0I", "420400", "999999999", "3", "921", true),
            row("0I", "030101", "958935420", "3", "921", false),
            row("0K", "420400", "999999999", "3", "921", true),
            row("0K", "030101", "958935420", "3", "921", false),
            row("0M", "420400", "999999999", "3", "921", true),
            row("0M", "030101", "958935420", "3", "921", false),
            row("0P", "420400", "999999999", "3", "921", true),
            row("0P", "030101", "958935420", "3", "921", false),
        ) { skjema, region, orgnr, kontoklasse, art, expectedResult ->
            When("For $skjema, $region, $orgnr, $kontoklasse, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_REGION to region,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                            RegnskapConstants.FIELD_ART to art,
                        )
                    )
                )

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér ugyldig art '${art}' " +
                                    "i driftsregnskapet til en gyldig art i driftsregnskapet eller overfør posteringen " +
                                    "til investeringsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})