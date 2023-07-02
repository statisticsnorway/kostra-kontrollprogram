package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule080KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    Given("context") {
        val sut = Rule080KombinasjonBevilgningFunksjonArt()

        forAll(
            row(
                "matches isBevilgningRegnskap, art, funksjon, belop",
                "0A", "100 ", "800", "1", true
            ),
            row(
                "does not match isBevilgningRegnskap",
                "0X", "100 ", "800", "1", false
            ),
            row(
                "does not match art",
                "0A", "100 ", "801", "1", false
            ),
            row(
                "does not match funksjon",
                "0A", "840 ", "800", "1", false
            ),
            row(
                "does not match belop",
                "0A", "100 ", "800", "0", false
            )
        ) { description, skjema, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_FUNKSJON to funksjon,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_BELOP to belop,
            ).toKostraRecord().asList()

            When("$description for $skjema, $funksjon, $art -> $expectError") {
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