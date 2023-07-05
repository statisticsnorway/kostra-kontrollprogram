package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule150AvskrivningerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule150Avskrivninger()

        forAll(
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches, art matches, belop matches",
                kostraRecordInTest("420400", "1", "100 ", "590", "0"),
                true, Severity.ERROR
            ),
            row(
                "isOsloBydel = true, isBevilgningDriftRegnskap = true, funksjon matches, art matches, belop matches",
                kostraRecordInTest("030101", "1", "100 ", "590", "0"),
                false, Severity.INFO
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = false, funksjon matches, art matches, belop matches",
                kostraRecordInTest("420400", "0", "100 ", "590", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon not matching #1, art matches, belop matches",
                kostraRecordInTest("420400", "1", "099 ", "590", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon not matching #2, art matches, belop matches",
                kostraRecordInTest("420400", "1", "099 ", "800", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches, art not matching, belop matches",
                kostraRecordInTest("420400", "1", "100 ", "591", "0"),
                false, Severity.ERROR
            ),
            row(
                "isOsloBydel = false, isBevilgningDriftRegnskap = true, funksjon matches, art matches, belop not matching",
                kostraRecordInTest("420400", "1", "100 ", "590", "1"),
                false, Severity.ERROR
            ),
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
            kontoklasse: String,
            funksjon: String,
            art: String,
            belop: String
        ) = mapOf(
            RegnskapConstants.FIELD_REGION to region,
            RegnskapConstants.FIELD_SKJEMA to "0A",
            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
            RegnskapConstants.FIELD_FUNKSJON to funksjon,
            RegnskapConstants.FIELD_ART to art,
            RegnskapConstants.FIELD_BELOP to belop
        ).toKostraRecord().asList()
    }
}