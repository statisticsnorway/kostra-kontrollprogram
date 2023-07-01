package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule080KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule080KombinasjonBevilgningFunksjonArt()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row("0A", "840 ", "010", "1", false),
            row("0A", "840 ", "800", "1", false),
            row("0A", "100 ", "800", "1", true),
            row("0C", "840 ", "010", "1", false),
            row("0C", "840 ", "800", "1", false),
            row("0C", "100 ", "800", "1", true),
            row("0I", "840 ", "010", "1", false),
            row("0I", "840 ", "800", "1", false),
            row("0I", "100 ", "800", "1", true),
            row("0K", "840 ", "010", "1", false),
            row("0K", "840 ", "800", "1", false),
            row("0K", "100 ", "800", "1", true),
            row("0M", "840 ", "010", "1", false),
            row("0M", "840 ", "800", "1", false),
            row("0M", "100 ", "800", "1", true),
            row("0P", "840 ", "010", "1", false),
            row("0P", "840 ", "800", "1", false),
            row("0P", "100 ", "800", "1", true)
        ) { skjema, funksjon, art, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_FUNKSJON to funksjon,
                        RegnskapConstants.FIELD_ART to art,
                        RegnskapConstants.FIELD_BELOP to belop,
                    )
                )
            )

            When("For $skjema, $funksjon, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Art 800 er kun tillat brukt i kombinasjon med funksjon 840."
                )
            }
        }
    }
})