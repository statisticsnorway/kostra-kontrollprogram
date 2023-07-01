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
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule160AvskrivningerAndreFunksjonerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule160AvskrivningerAndreFunksjoner()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row("420400", "0A", "1", "800 ", "590", "1", true),
            row("420400", "0A", "1", "800 ", "590", "0", false),
            row("030101", "0A", "1", "800 ", "590", "1", false),
            row("420400", "0C", "1", "800 ", "590", "1", true),
            row("420400", "0C", "1", "800 ", "590", "0", false),
            row("420400", "0I", "3", "800 ", "590", "1", true),
            row("420400", "0I", "3", "800 ", "590", "0", false),
            row("420400", "0K", "3", "800 ", "590", "1", true),
            row("420400", "0K", "3", "800 ", "590", "0", false),
            row("420400", "0M", "3", "800 ", "590", "1", true),
            row("420400", "0M", "3", "800 ", "590", "0", false),
            row("420400", "0P", "3", "800 ", "590", "1", true),
            row("420400", "0P", "3", "800 ", "590", "0", false)
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        FIELD_REGION to region,
                        FIELD_SKJEMA to skjema,
                        FIELD_KONTOKLASSE to kontoklasse,
                        FIELD_FUNKSJON to funksjon,
                        FIELD_ART to art,
                        FIELD_BELOP to belop
                    )
                )
            )

            When("For $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at avskrivningene ($belop) føres på " +
                            "tjenestefunksjon og ikke på funksjonene ([${funksjon.trim()}])"
                )
            }
        }
    }
})