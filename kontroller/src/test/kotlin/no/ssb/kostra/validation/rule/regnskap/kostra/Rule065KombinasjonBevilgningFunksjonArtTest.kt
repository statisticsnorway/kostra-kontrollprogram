package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule065KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule065KombinasjonBevilgningFunksjonArt()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row("0A", "899 ", "010", "1", true),
            row("0A", "899 ", "589", "1", false),
            row("0A", "899 ", "980", "1", false),
            row("0A", "899 ", "989", "1", false),
            row("0A", "100 ", "589", "1", true),
            row("0A", "100 ", "980", "1", true),
            row("0A", "100 ", "989", "1", true),
            row("0C", "899 ", "010", "1", true),
            row("0C", "899 ", "589", "1", false),
            row("0C", "899 ", "980", "1", false),
            row("0C", "899 ", "989", "1", false),
            row("0C", "100 ", "589", "1", true),
            row("0C", "100 ", "980", "1", true),
            row("0C", "100 ", "989", "1", true),
            row("0I", "899 ", "010", "1", true),
            row("0I", "899 ", "589", "1", false),
            row("0I", "899 ", "980", "1", false),
            row("0I", "899 ", "989", "1", false),
            row("0I", "100 ", "589", "1", true),
            row("0I", "100 ", "980", "1", true),
            row("0I", "100 ", "989", "1", true),
            row("0K", "899 ", "010", "1", true),
            row("0K", "899 ", "589", "1", false),
            row("0K", "899 ", "980", "1", false),
            row("0K", "899 ", "989", "1", false),
            row("0K", "100 ", "589", "1", true),
            row("0K", "100 ", "980", "1", true),
            row("0K", "100 ", "989", "1", true),
            row("0M", "899 ", "010", "1", true),
            row("0M", "899 ", "589", "1", false),
            row("0M", "899 ", "980", "1", false),
            row("0M", "899 ", "989", "1", false),
            row("0M", "100 ", "589", "1", true),
            row("0M", "100 ", "980", "1", true),
            row("0M", "100 ", "989", "1", true),
            row("0P", "899 ", "010", "1", true),
            row("0P", "899 ", "589", "1", false),
            row("0P", "899 ", "980", "1", false),
            row("0P", "899 ", "989", "1", false),
            row("0P", "100 ", "589", "1", true),
            row("0P", "100 ", "980", "1", true),
            row("0P", "100 ", "989", "1", true)
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

            When("For $skjema, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    expectedErrorText = "Artene 589, 980 og 989 er kun tillat brukt i kombinasjon med funksjon 899. " +
                            "Og motsatt, funksjon 899 er kun tillat brukt i kombinasjon med artene 589, 980 og 989."
                )
            }
        }
    }
})