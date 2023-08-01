package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule135RammetilskuddTest : BehaviorSpec({
    Given("context") {
        val sut = Rule135Rammetilskudd()

        forAll(
            row(
                "matches region, isRegional (0A), isBevilgningDriftRegnskap (0A, 1), funksjon (840 ), art (800), belop (0)",
                "420400", "0A", "1", "840 ", "800", "0", true
            ),
            row(
                "region does not match, Oslo",
                "030101", "0A", "1", "840 ", "800", "0", false
            ),
            row(
                "region does not match, Longyearbyen",
                "211100", "0A", "1", "840 ", "800", "0", false
            ),
            row(
                "isRegional does not match",
                "420400", "0I", "1", "840 ", "800", "0", false
            ),
            row(
                "isBevilgningDriftRegnskap does not match",
                "420400", "0A", "0", "840 ", "800", "0", false
            ),
            row(
                "funksjon does not match",
                "420400", "0A", "1", "841 ", "800", "0", true
            ),
            row(
                "art does not match",
                "420400", "0A", "1", "840 ", "801", "0", true
            ),
            row(
                "belop does not match",
                "420400", "0A", "1", "840 ", "800", "-1", false
            )
        ) { description, region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_REGION to region,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("$description for $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder rammetilskudd ($belop)."
                )
            }
        }
    }
})