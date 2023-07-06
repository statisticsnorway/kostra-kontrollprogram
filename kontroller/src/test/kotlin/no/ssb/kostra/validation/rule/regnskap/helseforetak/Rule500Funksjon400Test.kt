package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ORGNR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule500Funksjon400Test : BehaviorSpec({
    val sut = Rule500Funksjon400(listOf("928033821"))

    Given("context") {
        forAll(
            row("0Y", "300 ", "987654321", false), // skjema mismatch -> OK
            row("0X", "300 ", "987654321", false), // funksjon mismatch -> OK
            row("0X", "400 ", "987654321", true), // orgnr mismatch -> Fail
            row("0X", "400 ", "928033821", false), // OK
        ) { skjema, funksjon, orgnr, expectError ->
            val kostraRecordList = mapOf(
                FIELD_ORGNR to orgnr,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to " ",
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to "300",
                FIELD_BELOP to "100"
            ).toKostraRecord().asList()

            When("$orgnr, $skjema, $funksjon") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Ugyldig funksjon. Funksjonen '400' kan kun benyttes av RHF og Nasjonale " +
                            "felleseide HF. Korriger funksjon."
                )
            }
        }
    }
})