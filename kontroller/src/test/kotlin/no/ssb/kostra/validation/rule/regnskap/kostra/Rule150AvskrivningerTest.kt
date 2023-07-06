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
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule150AvskrivningerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule150Avskrivninger()

        forAll(
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches #1, art matches, belop matches",
                kostraRecordInTest("420400", "0A", "1", "100 ", "590", "0"),
                true, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches #2, art matches, belop matches",
                kostraRecordInTest("420400", "0A", "1", "799 ", "590", "0"),
                true, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true #2, funksjon matches #2, art matches, belop matches",
                kostraRecordInTest("420400", "0I", "3", "799 ", "590", "0"),
                true, Severity.INFO
            ),
            row(
                "isOsloBydel = true, isBevilgningDriftRegnskap = true, funksjon matches, art matches, belop matches",
                kostraRecordInTest("030101", "0A", "1", "100 ", "590", "0"),
                false, Severity.INFO
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = false, funksjon matches, art matches, belop matches",
                kostraRecordInTest("420400", "0A", "0", "100 ", "590", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon mismatch #1, art matches, belop matches",
                kostraRecordInTest("420400", "0A", "1", "099 ", "590", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon mismatch #2, art matches, belop matches",
                kostraRecordInTest("420400", "0A", "1", "800 ", "590", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches, art mismatch, belop matches",
                kostraRecordInTest("420400", "0A", "1", "100 ", "591", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches, art matches, belop mismatch",
                kostraRecordInTest("420400", "0A", "1", "100 ", "590", "1"),
                false, Severity.ERROR
            )
        ) { description, kostraRecords, expectError, expectedSeverity ->
            When("testing $description") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecords),
                    expectError = expectError,
                    expectedSeverity = expectedSeverity,
                    "Korrigér i fila slik at den inneholder avskrivninger " +
                            "(0), føres på tjenestefunksjon og art 590."
                )
            }
        }
    }
}) {
    companion object {
        private fun kostraRecordInTest(
            region: String,
            skjema: String,
            kontoklasse: String,
            funksjon: String,
            art: String,
            belop: String
        ) = mapOf(
            FIELD_REGION to region,
            FIELD_SKJEMA to skjema,
            FIELD_KONTOKLASSE to kontoklasse,
            FIELD_FUNKSJON to funksjon,
            FIELD_ART to art,
            FIELD_BELOP to belop
        ).toKostraRecord().asList()
    }
}