package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule075KombinasjonBevilgningFunksjonArt.Companion.REQUIRED_FUNCTION
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule075KombinasjonBevilgningFunksjonArt.Companion.qualifyingArtCodes

class Rule075KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule075KombinasjonBevilgningFunksjonArt()

        forAll(
            *qualifyingArtCodes.map {
                row(
                    "bevilgningregnskap = true, art, funksjon, belop matcher, art = $it",
                    "0A", "100 ", it, "1", true
                )
            }.toTypedArray(),
            row(
                "bevilgningregnskap = false",
                "0X", "100 ", "870", "1", false
            ),
            row(
                "bevilgningregnskap = true, art is not matching",
                "0A", "100 ", "877", "1", false
            ),
            row(
                "bevilgningregnskap = true, art is matching, funksjon is not matching",
                "0A", REQUIRED_FUNCTION, "870", "1", false
            ),
            row(
                "bevilgningregnskap = true, art, funksjon is matching, belop is not matching",
                "0A", "100 ", "870", "0", false
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

            When("$description For $skjema, $funksjon, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Artene 870, 871, 872, 873, 875, 876 er kun tillat brukt i kombinasjon med funksjon 800."
                )
            }
        }
    }
})