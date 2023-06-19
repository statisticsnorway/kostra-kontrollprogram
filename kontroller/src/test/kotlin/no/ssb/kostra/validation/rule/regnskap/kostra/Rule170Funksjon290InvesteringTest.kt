package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule170Funksjon290InvesteringTest : BehaviorSpec({
    Given("context") {
        val sut = Rule170Funksjon290Investering()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("420400", "0A", "0", "290 ", "010", "31", true),
            row("420400", "0A", "0", "290 ", "010", "30", false),
            row("030101", "0A", "0", "290 ", "010", "31", false),
            row("420400", "0C", "0", "290 ", "010", "31", false),
            row("420400", "0C", "0", "290 ", "010", "30", false),
            row("420400", "0I", "4", "290 ", "010", "31", false),
            row("420400", "0I", "4", "290 ", "010", "30", false),
            row("420400", "0K", "4", "290 ", "010", "31", false),
            row("420400", "0K", "4", "290 ", "010", "30", false),
            row("420400", "0M", "4", "290 ", "010", "31", true),
            row("420400", "0M", "4", "290 ", "010", "30", false),
            row("420400", "0P", "4", "290 ", "010", "31", false),
            row("420400", "0P", "4", "290 ", "010", "30", false),
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectedResult ->
            When("For $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
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

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér i fila slik at differanse ($belop) på funksjon " +
                                    "290 interkommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')"
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})