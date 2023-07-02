package no.ssb.kostra.validation.rule.regnskap.kirkekostra

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

class Rule113SummeringTilskuddTest : BehaviorSpec({
    val sut = Rule113SummeringTilskudd()

    Given("context") {
        forAll(
            row("420400", "0F", "3", "041 ", "590", "0", true), // art mismatch
            row("420400", "0F", "3", "041 ", "830", "0", true), // belop = 0
            row("420400", "0F", "3", "041 ", "830", "1", false), // belop OK
            row("420400", "42", "3", "041 ", "830", "0", false), // skjema mismatch
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_REGION to region,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop
            ).toKostraRecord().asList()

            When("$region, $skjema, $kontoklasse, $funksjon, $art, $belop") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder tilskudd ($belop) fra kommunen"
                )
            }
        }
    }
})