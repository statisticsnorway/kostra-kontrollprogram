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

class Rule050KombinasjonInvesteringKontoklasseArtTest : BehaviorSpec({
    Given("context") {
        val invalidInvesteringArtList = listOf(
            "070", "080", "110", "114", "240", "509", "570", "590",
            "600", "629", "630", "640", "800", "870", "874", "875", "877", "909", "990"
        )
        val sut = Rule050KombinasjonInvesteringKontoklasseArt(invalidInvesteringArtList)

        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0A", "0", "010", "1", false),
            row("0A", "0", "070", "1", true),
            row("0A", "0", "990", "1", true),
            row("0C", "0", "010", "1", false),
            row("0C", "0", "070", "1", true),
            row("0C", "0", "990", "1", true),
            row("0I", "4", "010", "1", false),
            row("0I", "4", "070", "1", true),
            row("0I", "4", "990", "1", true),
            row("0K", "4", "010", "1", false),
            row("0K", "4", "070", "1", true),
            row("0K", "4", "990", "1", true),
            row("0M", "4", "010", "1", false),
            row("0M", "4", "070", "1", true),
            row("0M", "4", "990", "1", true),
            row("0P", "4", "010", "1", false),
            row("0P", "4", "070", "1", true),
            row("0P", "4", "990", "1", true),
        ) { skjema, kontoklasse, art, belop, expectedResult ->
            When("For $skjema, $kontoklasse, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
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
                            "Korrigér ugyldig art '${art}' i investeringsregnskapet til en gyldig art i " +
                                    "investeringsregnskapet eller overfør posteringen til driftsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})