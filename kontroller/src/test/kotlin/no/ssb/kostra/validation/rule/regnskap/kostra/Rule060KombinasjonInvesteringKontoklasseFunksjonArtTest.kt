package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule060KombinasjonInvesteringKontoklasseFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule060KombinasjonInvesteringKontoklasseFunksjonArt()

        forAll(
            row(
                "isBevilgningInvesteringRegnskap = true, art, funksjon, belop matches",
                "0A", "0", "100 ", "729", "1", true
            ),
            row(
                "isBevilgningInvesteringRegnskap = false",
                "0A", "1", "100 ", "729", "1", false
            ),
            row(
                "art is not matching",
                "0A", "0", "100 ", "728", "1", false
            ),
            row(
                "funksjon is not matching",
                "0A", "0", "841 ", "729", "1", false
            ),
            row(
                "belop is not matching",
                "0A", "0", "100 ", "729", "0", false
            )
        ) { description, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                RegnskapConstants.FIELD_FUNKSJON to funksjon,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_BELOP to belop,
            ).toKostraRecord().asList()

            When("$description for $skjema, $kontoklasse, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér til riktig kombinasjon av kontoklasse, funksjon og art. " +
                            "Art 729 er kun gyldig i kombinasjon med funksjon 841 i investeringsregnskapet."
                )
            }
        }
    }
})