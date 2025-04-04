package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule075KombinasjonBevilgningFunksjonArt.Companion.REQUIRED_FUNCTION
import no.ssb.kostra.validation.rule.regnskap.kostra.Rule075KombinasjonBevilgningFunksjonArt.Companion.qualifyingArtCodes

class Rule075KombinasjonBevilgningFunksjonArtTest :
    BehaviorSpec({
        Given("context") {
            val sut = Rule075KombinasjonBevilgningFunksjonArt()

            forAll(
                *qualifyingArtCodes
                    .map {
                        row(
                            "bevilgningregnskap = true, art, funksjon, belop matcher, art = $it",
                            "0A",
                            "100 ",
                            it,
                            "1",
                            true,
                        )
                    }.toTypedArray(),
                row(
                    "bevilgningregnskap = false",
                    "0X",
                    "100 ",
                    "870",
                    "1",
                    false,
                ),
                row(
                    "bevilgningregnskap = true, art mismatch",
                    "0A",
                    "100 ",
                    "877",
                    "1",
                    false,
                ),
                row(
                    "bevilgningregnskap = true, art match, funksjon mismatch",
                    "0A",
                    REQUIRED_FUNCTION,
                    "870",
                    "1",
                    false,
                ),
                row(
                    "bevilgningregnskap = true, art, funksjon match, belop mismatch",
                    "0A",
                    "100 ",
                    "870",
                    "0",
                    false,
                ),
            ) { description, skjema, funksjon, art, belop, expectError ->
                val kostraRecordList =
                    mapOf(
                        FIELD_SKJEMA to skjema,
                        FIELD_FUNKSJON to funksjon,
                        FIELD_ART to art,
                        FIELD_BELOP to belop,
                    ).toKostraRecord(1, fieldDefinitions).asList()

                When("$description For $skjema, $funksjon, $art -> $expectError") {
                    verifyValidationResult(
                        validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                        expectError = expectError,
                        expectedSeverity = Severity.ERROR,
                        "Artene 870, 871, 872, 873, 876, 878, 879 er kun tillat brukt i kombinasjon med funksjon 800.",
                    )
                }
            }
        }
    })
