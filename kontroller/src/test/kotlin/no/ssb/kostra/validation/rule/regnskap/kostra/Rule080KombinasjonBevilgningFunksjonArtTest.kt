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
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

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
                FIELD_SKJEMA to skjema,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop,
            ).toKostraRecord(1, fieldDefinitions).asList()

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