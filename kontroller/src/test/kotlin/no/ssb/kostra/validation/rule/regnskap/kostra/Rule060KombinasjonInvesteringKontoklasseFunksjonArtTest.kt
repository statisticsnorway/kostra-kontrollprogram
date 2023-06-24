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

class Rule060KombinasjonInvesteringKontoklasseFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule060KombinasjonInvesteringKontoklasseFunksjonArt()

        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("0A", "0", "100 ", "729", "1", true),
            row("0A", "0", "841 ", "010", "1", false),
            row("0A", "0", "841 ", "729", "1", false),
            row("0A", "1", "100 ", "729", "1", false),
            row("0C", "0", "100 ", "729", "1", true),
            row("0C", "0", "841 ", "010", "1", false),
            row("0C", "0", "841 ", "729", "1", false),
            row("0C", "1", "100 ", "729", "1", false),
            row("0I", "4", "100 ", "729", "1", true),
            row("0I", "4", "841 ", "010", "1", false),
            row("0I", "4", "841 ", "729", "1", false),
            row("0I", "3", "100 ", "729", "1", false),
            row("0K", "4", "100 ", "729", "1", true),
            row("0K", "4", "841 ", "010", "1", false),
            row("0K", "4", "841 ", "729", "1", false),
            row("0K", "3", "100 ", "729", "1", false),
            row("0M", "4", "100 ", "729", "1", true),
            row("0M", "4", "841 ", "010", "1", false),
            row("0M", "4", "841 ", "729", "1", false),
            row("0M", "3", "100 ", "729", "1", false),
            row("0P", "4", "100 ", "729", "1", true),
            row("0P", "4", "841 ", "010", "1", false),
            row("0P", "4", "841 ", "729", "1", false),
            row("0P", "3", "100 ", "729", "1", false),
        ) { skjema, kontoklasse, funksjon, art, belop, expectedResult ->
            When("For $skjema, $kontoklasse, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                            RegnskapConstants.FIELD_FUNKSJON to funksjon,
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
                            "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. " +
                                    "Art 729 er kun gyldig i kombinasjon med funksjon 841 i investeringsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})