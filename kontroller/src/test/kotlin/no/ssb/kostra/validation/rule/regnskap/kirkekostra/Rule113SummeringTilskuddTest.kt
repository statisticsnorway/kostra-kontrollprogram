package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule113SummeringTilskuddTest : BehaviorSpec({
    val sut = Rule113SummeringTilskudd()

    Given("context") {
        forAll(
            row("420400", "0F", "3", "041 ", "590", "0", true), // art mismatch
            row("420400", "0F", "3", "041 ", "830", "0", true), // belop = 0
            row("420400", "0F", "3", "041 ", "830", "1", false), // belop OK
            row("420400", "42", "3", "041 ", "830", "0", false), // skjema mismatch
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectError ->

            val kostraRecordList = listOf(
                KostraRecord(
                    index = 0,
                    fieldDefinitionByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name },
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

            When("$region, $skjema, $kontoklasse, $funksjon, $art, $belop") {
                val validationReportEntries = sut.validate(kostraRecordList)

                Then("expected result should be equal to $expectError") {
                    if (expectError) {
                        validationReportEntries.shouldNotBeNull()

                        assertSoftly(validationReportEntries.first()) {
                            severity.shouldBeEqual(Severity.ERROR)
                            messageText shouldBe "Korrigér slik at fila inneholder tilskudd ($belop) fra kommunen"
                        }
                    } else validationReportEntries.shouldBeNull()
                }
            }
        }
    }
})