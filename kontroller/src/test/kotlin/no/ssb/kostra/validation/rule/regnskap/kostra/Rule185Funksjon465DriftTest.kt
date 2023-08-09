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

class Rule185Funksjon465DriftTest : BehaviorSpec({
    Given("context") {
        val sut = Rule185Funksjon465Drift()

        forAll(
            row("420000", "0A", "1", "465 ", "010", "31", false),
            row("420000", "0A", "1", "465 ", "010", "30", false),
            row("030101", "0A", "1", "465 ", "010", "31", false),
            row("420000", "0C", "1", "465 ", "010", "31", true),
            row("420000", "0C", "1", "465 ", "010", "30", false),
            row("420000", "0I", "3", "465 ", "010", "31", false),
            row("420000", "0I", "3", "465 ", "010", "30", false),
            row("420000", "0K", "3", "465 ", "010", "31", false),
            row("420000", "0K", "3", "465 ", "010", "30", false),
            row("420000", "0M", "3", "465 ", "010", "31", false),
            row("420000", "0M", "3", "465 ", "010", "30", false),
            row("420000", "0P", "3", "465 ", "010", "31", true),
            row("420000", "0P", "3", "465 ", "010", "30", false),
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_REGION to region,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("For $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at differanse ($belop) på funksjon 465 " +
                            "Interfylkeskommunale samarbeid (§§ 27/28a-samarbeid) går i 0 i driftsregnskapet. " +
                            "(margin på +/- 30')"
                )
            }
        }
    }
})