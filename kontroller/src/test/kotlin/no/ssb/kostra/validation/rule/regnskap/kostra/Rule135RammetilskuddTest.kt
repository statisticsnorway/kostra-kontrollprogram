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

class Rule135RammetilskuddTest : BehaviorSpec({
    Given("context") {
        val sut = Rule135Rammetilskudd()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("420400", "0A", "1", "840 ", "800", "-1", false),
            row("420400", "0A", "1", "840 ", "800", "0", true),
            row("030101", "0A", "1", "840 ", "800", "0", false),
            row("211100", "0A", "1", "840 ", "800", "0", false),
            row("420400", "0C", "1", "840 ", "800", "-1", false),
            row("420400", "0C", "1", "840 ", "800", "0", true),
            row("420400", "0I", "3", "840 ", "800", "-1", false),
            row("420400", "0I", "3", "840 ", "800", "0", false),
            row("420400", "0K", "3", "840 ", "800", "-1", false),
            row("420400", "0K", "3", "840 ", "800", "0", false),
            row("420400", "0M", "3", "840 ", "800", "-1", false),
            row("420400", "0M", "3", "840 ", "800", "0", true),
            row("420400", "0P", "3", "840 ", "800", "-1", false),
            row("420400", "0P", "3", "840 ", "800", "0", true),
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
                            "Korrigér slik at fila inneholder rammetilskudd ($belop)."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})