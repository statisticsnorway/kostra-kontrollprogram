package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule200Funksjon089FinansieringstransaksjonerTest : BehaviorSpec({

    Given("context") {
        val sut = Rule200Funksjon089Finansieringstransaksjoner()

        forAll(
            row("0F", "3", "089 ", "500", false),
            row("0F", "3", "089 ", "100", true),
        ) { skjema, kontoklasse, funksjon, art, expectError ->
            val kostraRecordList = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
            ).toKostraRecord().asList()

            When("For $skjema, $kontoklasse, $funksjon, $art -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at art ($art) " +
                            "er gyldig mot funksjon 089. Gyldige arter er 500-580, 830 og 900-980."
                )
            }
        }
    }
})