package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule165AvskrivningerMotpostAvskrivningerAndreFunksjonerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule165AvskrivningerMotpostAvskrivningerAndreFunksjoner()

        forAll(
            row("420400", "0A", "1", "100 ", "990", "1", true),
            row("420400", "0A", "1", "100 ", "990", "0", false),
            row("030101", "0A", "1", "100 ", "990", "1", false),
            row("420400", "0C", "1", "100 ", "990", "1", true),
            row("420400", "0C", "1", "100 ", "990", "0", false),
            row("420400", "0I", "3", "100 ", "990", "1", true),
            row("420400", "0I", "3", "100 ", "990", "0", false),
            row("420400", "0K", "3", "100 ", "990", "1", true),
            row("420400", "0K", "3", "100 ", "990", "0", false),
            row("420400", "0M", "3", "100 ", "990", "1", true),
            row("420400", "0M", "3", "100 ", "990", "0", false),
            row("420400", "0P", "3", "100 ", "990", "1", true),
            row("420400", "0P", "3", "100 ", "990", "0", false)
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_REGION to region,
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                RegnskapConstants.FIELD_FUNKSJON to funksjon,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_BELOP to belop
            ).toKostraRecord().asList()

            When("For $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at motpost avskrivninger ($belop) kun er " +
                            "ført på funksjon 860, art 990 og ikke på funksjonene ([${funksjon.trim()}])"
                )
            }
        }
    }
})