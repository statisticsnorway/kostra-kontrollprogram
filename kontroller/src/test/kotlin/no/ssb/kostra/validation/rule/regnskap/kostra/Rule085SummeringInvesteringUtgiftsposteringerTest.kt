package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.plus

class Rule085SummeringInvesteringUtgiftsposteringerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule085SummeringInvesteringUtgiftsposteringer()
        val fieldDefinitionsByName = listOf(
            FieldDefinition(from = 1, to = 2, name = RegnskapConstants.FIELD_SKJEMA),
            FieldDefinition(from = 3, to = 3, name = RegnskapConstants.FIELD_KONTOKLASSE),
            FieldDefinition(from = 4, to = 6, name = RegnskapConstants.FIELD_ART),
            FieldDefinition(from = 7, to = 10, name = RegnskapConstants.FIELD_BELOP)
        ).associateBy { it.name }

        forAll(
            row("0A", "0", "590", "0", true),
            row("0C", "0", "590", "0", true),
            row("0M", "4", "590", "0", true),
            row("0P", "4", "590", "0", true),

            row("0A", "0", "600", "0", false),
            row("0C", "0", "600", "0", false),
            row("0M", "4", "600", "0", false),
            row("0P", "4", "600", "0", false),

            row("0A", "0", "010", "1", false),
            row("0A", "0", "590", "1", false),
            row("0C", "0", "010", "1", false),
            row("0C", "0", "590", "1", false),
            row("0I", "4", "010", "1", false),
            row("0I", "4", "590", "1", false),
            row("0I", "4", "590", "0", false),
            row("0K", "4", "010", "1", false),
            row("0K", "4", "590", "1", false),
            row("0K", "4", "590", "0", false),
            row("0M", "4", "010", "1", false),
            row("0M", "4", "590", "1", false),
            row("0P", "4", "010", "1", false),
            row("0P", "4", "590", "1", false)

        ) { skjema, kontoklasse, art, belop, expectError  ->
            When("Expenses is zero for $skjema, $kontoklasse, $art, $belop") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                            RegnskapConstants.FIELD_ART to art,
                            RegnskapConstants.FIELD_BELOP to belop
                        )
                    )
                )

                Then("validation should pass with errors") {
                    if (expectError) {
                        sut.validate(kostraRecordList).shouldNotBeNull()
                    } else {
                        sut.validate(kostraRecordList).shouldBeNull()
                    }
                }
            }
        }
    }
})