package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.FieldDefinition
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.regnskapRecordInTest

class Rule020KombinasjonDriftKontoklasseFunksjonTest : BehaviorSpec({

    Given("context") {
        val sut = Rule020KombinasjonDriftKontoklasseFunksjon(listOf("841 "))

        forAll(
            row("0A", "1", "100 ", "1", false),
            row("0A", "1", "841 ", "1", true),
            row("0C", "1", "100 ", "1", false),
            row("0C", "1", "841 ", "1", true),
            row("0I", "3", "100 ", "1", false),
            row("0I", "3", "841 ", "1", true),
            row("0K", "3", "100 ", "1", false),
            row("0K", "3", "841 ", "1", true),
            row("0M", "3", "100 ", "1", false),
            row("0M", "3", "841 ", "1", true),
            row("0P", "3", "100 ", "1", false),
            row("0P", "3", "841 ", "1", true)
        ) { skjema, kontoklasse, funksjon, belop, expectError ->
            val kostraRecordList = regnskapRecordInTest(
                fieldDefinitions = listOf(
                    FieldDefinition(from = 1, to = 2, name = FIELD_SKJEMA),
                    FieldDefinition(from = 3, to = 3, name = FIELD_KONTOKLASSE),
                    FieldDefinition(from = 4, to = 7, name = FIELD_FUNKSJON),
                ),
                valuesByName = mapOf(
                    FIELD_SKJEMA to skjema,
                    FIELD_KONTOKLASSE to kontoklasse,
                    FIELD_FUNKSJON to funksjon,
                    FIELD_BELOP to belop,
                )
            ).asList()

            When("For $skjema, $kontoklasse, $funksjon -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér ugyldig funksjon '${funksjon}' i driftsregnskapet " +
                            "til en gyldig funksjon i driftsregnskapet eller overfør posteringen til " +
                            "investeringsregnskapet."
                )
            }
        }
    }
})