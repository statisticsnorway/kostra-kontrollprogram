package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule045KombinasjonInvesteringKontoklasseFunksjonTest : BehaviorSpec({

    Given("context") {
        val sut = Rule045KombinasjonInvesteringKontoklasseFunksjon(
            listOf("100 ", "110 ", "121 ", "170 ", "171 ", "400 ", "410 ", "421 ", "470 ", "471 ")
        )
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA),
            FieldDefinition(from = 3, to = 3, name = RegnskapConstants.FIELD_KONTOKLASSE),
            FieldDefinition(from = 4, to = 7, name = RegnskapConstants.FIELD_FUNKSJON),
        ).associateBy { it.name }

        forAll(
            row("0A", "0", "100 ", true),
            row("0A", "0", "400 ", true),
            row("0A", "0", "201 ", false),
            row("0A", "0", "510 ", false),
            row("0C", "0", "100 ", true),
            row("0C", "0", "400 ", true),
            row("0C", "0", "201 ", false),
            row("0C", "0", "510 ", false),
            row("0I", "4", "100 ", true),
            row("0I", "4", "400 ", true),
            row("0I", "4", "201 ", false),
            row("0I", "4", "510 ", false),
            row("0K", "4", "100 ", true),
            row("0K", "4", "400 ", true),
            row("0K", "4", "201 ", false),
            row("0K", "4", "510 ", false),
            row("0M", "4", "100 ", true),
            row("0M", "4", "400 ", true),
            row("0M", "4", "201 ", false),
            row("0M", "4", "510 ", false),
            row("0P", "4", "100 ", true),
            row("0P", "4", "400 ", true),
            row("0P", "4", "201 ", false),
            row("0P", "4", "510 ", false),
        ) { skjema, kontoklasse, funksjon, expectedResult ->
            When("For $skjema, $kontoklasse, $funksjon -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                            RegnskapConstants.FIELD_FUNKSJON to funksjon,
                        )
                    )
                )

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.INFO)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Kun advarsel, hindrer ikke innsending: (${funksjon}) regnes å være ulogisk " +
                                    "funksjon i investeringsregnskapet. Vennligst vurder å postere på annen funksjon " +
                                    "eller om posteringen hører til i driftsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})