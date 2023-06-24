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

class Rule150AvskrivningerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule150Avskrivninger()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("420400", "0A", "1", "100 ", "590", "1", false, Severity.INFO),
            row("420400", "0A", "1", "100 ", "590", "0", true, Severity.ERROR),
            row("030101", "0A", "1", "100 ", "590", "0", false, Severity.INFO),
            row("420400", "0C", "1", "100 ", "590", "1", false, Severity.INFO),
            row("420400", "0C", "1", "100 ", "590", "0", true, Severity.ERROR),
            row("420400", "0I", "3", "100 ", "590", "1", false, Severity.INFO),
            row("420400", "0I", "3", "100 ", "590", "0", true, Severity.INFO),
            row("420400", "0K", "3", "100 ", "590", "1", false, Severity.INFO),
            row("420400", "0K", "3", "100 ", "590", "0", true, Severity.INFO),
            row("420400", "0M", "3", "100 ", "590", "1", false, Severity.INFO),
            row("420400", "0M", "3", "100 ", "590", "0", true, Severity.ERROR),
            row("420400", "0P", "3", "100 ", "590", "1", false, Severity.INFO),
            row("420400", "0P", "3", "100 ", "590", "0", true, Severity.ERROR),
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectedResult, expectedSeverity ->
            When("$region, $skjema, $kontoklasse, $funksjon, $art, $belop, $expectedResult, $expectedSeverity") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_REGION to region,
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                            RegnskapConstants.FIELD_FUNKSJON to funksjon,
                            RegnskapConstants.FIELD_ART to art,
                            RegnskapConstants.FIELD_BELOP to belop
                        )
                    )
                )

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(expectedSeverity)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér i fila slik at den inneholder avskrivninger " +
                                    "($belop), føres på tjenestefunksjon og art 590."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})