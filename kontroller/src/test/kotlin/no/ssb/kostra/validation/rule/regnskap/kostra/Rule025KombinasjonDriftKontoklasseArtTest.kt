package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule025KombinasjonDriftKontoklasseArtTest : BehaviorSpec({

    Given("context") {
        val invalidDriftArtList =
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "921", "922", "929", "970")

        val sut = Rule025KombinasjonDriftKontoklasseArt(invalidDriftArtList)

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
        ) { skjema, region, orgnr, kontoklasse, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_REGION to region,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_ART to art,
                FIELD_BELOP to belop,
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("For $skjema, $region, $orgnr, $kontoklasse, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér ugyldig art '${art}' " +
                            "i driftsregnskapet til en gyldig art i driftsregnskapet eller overfør posteringen " +
                            "til investeringsregnskapet."
                )
            }
        }
    }

    Given("context lånefond") {
        val invalidLanefondDriftArtList =
            listOf("280", "512", "521", "522", "529", "670", "910", "911", "912", "922", "929", "970")

        val sut = Rule025KombinasjonDriftKontoklasseArt(invalidLanefondDriftArtList)

        forAll(
            *invalidLanefondDriftArtList.map {
                row(
                    "bevilgningregnskap = true, art and belop are match, art = $it",
                    "0A", "100 ", it, "1", true
                )
            }.toTypedArray(),
            row(
                "bevilgningregnskap = false",
                "0X", "100 ", "280", "1", false
            ),
            row(
                "bevilgningregnskap = true, art mismatch",
                "0A", "100 ", "199", "1", false
            ),
            row(
                "bevilgningregnskap = true, art match, belop mismatch",
                "0A", "100 ", "280", "0", false
            )
        ) { description, skjema, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop,
                FIELD_KONTOKLASSE to "1",
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("$description For $skjema, $funksjon, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér ugyldig art '${art}' " +
                            "i driftsregnskapet til en gyldig art i driftsregnskapet eller overfør posteringen " +
                            "til investeringsregnskapet."
                )
            }
        }
    }
})