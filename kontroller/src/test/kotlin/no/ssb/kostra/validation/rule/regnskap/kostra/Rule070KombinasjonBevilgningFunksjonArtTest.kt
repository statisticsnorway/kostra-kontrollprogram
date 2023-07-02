package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule070KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule070KombinasjonBevilgningFunksjonArt()

        forAll(
            row(
                "bevilgningregnskap, funksjon, art og beløp matcher",
                "0A", "899 ", "530", "1", true
            ),
            row(
                "bevilgningregnskap = false",
                "0X", "899 ", "530", "1", false
            ),
            row(
                "bevilgningregnskap = true, funksjon = 880",
                "0X", "880 ", "530", "1", false
            ),
            row(
                "bevilgningregnskap = true, funksjon matcher, art matcher ikke",
                "0X", "899 ", "010", "1", false
            ),
            row(
                "bevilgningregnskap = true, funksjon og art matcher, belop matcher ikke",
                "0X", "899 ", "530", "0", false
            )
        ) { description, skjema, funksjon, art, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name },
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_FUNKSJON to funksjon,
                        RegnskapConstants.FIELD_ART to art,
                        RegnskapConstants.FIELD_BELOP to belop,
                    )
                )
            )

            When("$description, $skjema, $funksjon, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Art 530 er kun tillat brukt i kombinasjon med funksjon 880"
                )
            }
        }
    }
})