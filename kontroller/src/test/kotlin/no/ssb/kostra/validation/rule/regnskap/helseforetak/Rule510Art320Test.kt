package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.area.regnskap.helseforetak.HelseForetakMain.Companion.art320Funksjoner
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils

class Rule510Art320Test : BehaviorSpec({
    val sut = Rule510Art320(art320Funksjoner)

    Given("context") {
        forAll(
            row("0Y", "100", "300 ", false), // skjema mismatch -> OK
            row("0X", "100", "300 ", false), // art mismatch -> OK
            row("0X", "320", "400 ", true), // funksjon mismatch -> Fail
            row("0X", "320", "620 ", false), // OK
        ) { skjema, art, funksjon, expectError ->
            val kostraRecordList = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_ART to art,
                FIELD_FUNKSJON to funksjon,
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("$skjema, $art, $funksjon") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Ugyldig funksjon. Kontokode 320 ISF inntekter kan kun benyttes av somatisk, "
                            + "psykisk helsevern og rus. Korriger funksjon til én av: "
                            + "[620, 630, 636, 637, 641, 642, 651, 681, 840]"
                )
            }
        }
    }
})