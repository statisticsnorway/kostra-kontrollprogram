package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule510Art320Test : BehaviorSpec({
    val sut = Rule510Art320(listOf("620"))

    Given("context") {
        forAll(
            row("0Y", "100", "300 ", false), // skjema mismatch -> OK
            row("0X", "100", "300 ", false), // art mismatch -> OK
            row("0X", "320", "400 ", true), // funksjon mismatch -> Fail
            row("0X", "320", "620 ", false), // OK
        ) { skjema, art, funksjon, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_FUNKSJON to funksjon,
            ).toKostraRecord().asList()

            When("$skjema, $art, $funksjon") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Ugyldig funksjon. Kontokode 320 ISF inntekter kan kun benyttes av somatisk, psykisk helsevern og rus. Korriger funksjon."
                )
            }
        }
    }
})