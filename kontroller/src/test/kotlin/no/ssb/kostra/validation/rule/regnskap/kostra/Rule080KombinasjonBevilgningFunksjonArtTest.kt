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

class Rule080KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule080KombinasjonBevilgningFunksjonArt()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0A", "840 ", "010", false),
            row("0A", "840 ", "800", false),
            row("0A", "100 ", "800", true),
            row("0C", "840 ", "010", false),
            row("0C", "840 ", "800", false),
            row("0C", "100 ", "800", true),
            row("0I", "840 ", "010", false),
            row("0I", "840 ", "800", false),
            row("0I", "100 ", "800", true),
            row("0K", "840 ", "010", false),
            row("0K", "840 ", "800", false),
            row("0K", "100 ", "800", true),
            row("0M", "840 ", "010", false),
            row("0M", "840 ", "800", false),
            row("0M", "100 ", "800", true),
            row("0P", "840 ", "010", false),
            row("0P", "840 ", "800", false),
            row("0P", "100 ", "800", true),

            ) { skjema, funksjon, art, expectedResult ->
            When("For $skjema, $funksjon, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_FUNKSJON to funksjon,
                            RegnskapConstants.FIELD_ART to art,
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
                            "Art 800 er kun tillat brukt i kombinasjon med funksjon 840."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})