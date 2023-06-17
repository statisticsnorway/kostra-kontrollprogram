package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord

class Rule090SummeringInvesteringInntektsposteringerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule090SummeringInvesteringInntektsposteringer()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0A", "0", "600", "-1", false),
            row("0A", "0", "990", "-1", false),
            row("0A", "0", "990", "0", true),
            row("0C", "0", "600", "-1", false),
            row("0C", "0", "990", "-1", false),
            row("0C", "0", "990", "0", true),
            row("0I", "4", "600", "-1", false),
            row("0I", "4", "990", "-1", false),
            row("0I", "4", "990", "0", false),
            row("0K", "4", "600", "-1", false),
            row("0K", "4", "990", "-1", false),
            row("0K", "4", "990", "0", false),
            row("0M", "4", "600", "-1", false),
            row("0M", "4", "990", "-1", false),
            row("0M", "4", "990", "0", true),
            row("0P", "4", "600", "-1", false),
            row("0P", "4", "990", "-1", false),
            row("0P", "4", "990", "0", true),

            ) { skjema, kontoklasse, art, belop, expectError ->
            When("Income is zero for $skjema, $kontoklasse, $art, $belop") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            FIELD_SKJEMA to skjema,
                            FIELD_KONTOKLASSE to kontoklasse,
                            FIELD_ART to art,
                            FIELD_BELOP to belop
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