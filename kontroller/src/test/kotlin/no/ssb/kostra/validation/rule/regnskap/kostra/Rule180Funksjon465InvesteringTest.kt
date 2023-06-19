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

class Rule180Funksjon465InvesteringTest : BehaviorSpec({
    Given("context") {
        val sut = Rule180Funksjon465Investering()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("420000", "0A", "0", "465 ", "010", "31", false),
            row("420000", "0A", "0", "465 ", "010", "30", false),
            row("030101", "0A", "0", "465 ", "010", "31", false),
            row("420000", "0C", "0", "465 ", "010", "31", true),
            row("420000", "0C", "0", "465 ", "010", "30", false),
            row("420000", "0I", "4", "465 ", "010", "31", false),
            row("420000", "0I", "4", "465 ", "010", "30", false),
            row("420000", "0K", "4", "465 ", "010", "31", false),
            row("420000", "0K", "4", "465 ", "010", "30", false),
            row("420000", "0M", "4", "465 ", "010", "31", false),
            row("420000", "0M", "4", "465 ", "010", "30", false),
            row("420000", "0P", "4", "465 ", "010", "31", true),
            row("420000", "0P", "4", "465 ", "010", "30", false),
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectedResult ->
            When("For $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
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
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér i fila slik at differanse ($belop) på funksjon 465 " +
                                    "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                                    "(margin på +/- 30')"
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})