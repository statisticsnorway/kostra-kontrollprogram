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

class Rule070KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule070KombinasjonBevilgningFunksjonArt()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0A", "899 ", "010", "1", true),
            row("0A", "899 ", "530", "1", true),
            row("0A", "880 ", "530", "1", false),
            row("0A", "899 ", "980", "1", false),
            row("0C", "899 ", "530", "1", true),
            row("0C", "880 ", "530", "1", false),
            row("0C", "899 ", "980", "1", false),
            row("0I", "899 ", "530", "1", true),
            row("0I", "880 ", "530", "1", false),
            row("0I", "899 ", "980", "1", false),
            row("0K", "899 ", "530", "1", true),
            row("0K", "880 ", "530", "1", false),
            row("0K", "899 ", "980", "1", false),
            row("0M", "899 ", "530", "1", true),
            row("0M", "880 ", "530", "1", false),
            row("0M", "899 ", "980", "1", false),
            row("0P", "899 ", "530", "1", true),
            row("0P", "880 ", "530", "1", false),
            row("0P", "899 ", "980", "1", false),
        ) { skjema, funksjon, art, belop, expectedResult ->
            When("For $skjema, $funksjon, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            RegnskapConstants.FIELD_SKJEMA to skjema,
                            RegnskapConstants.FIELD_FUNKSJON to funksjon,
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
                            "Art 530 er kun tillat brukt i kombinasjon med funksjon 880"
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})