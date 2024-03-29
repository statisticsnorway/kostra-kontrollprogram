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
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule105SummeringDriftInntektsposteringerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule105SummeringDriftInntektsposteringer()

        forAll(
            row("420400", "0A", "1", "100 ", "600", "-1", false),
            row("420400", "0A", "1", "100 ", "990", "-1", false),
            row("420400", "0A", "1", "100 ", "990", "0", true),
            row("030101", "0A", "1", "100 ", "990", "0", false),
            row("420400", "0C", "1", "100 ", "600", "-1", false),
            row("420400", "0C", "1", "100 ", "990", "-1", false),
            row("420400", "0C", "1", "100 ", "990", "0", true),
            row("420400", "0I", "3", "100 ", "600", "-1", false),
            row("420400", "0I", "3", "100 ", "990", "-1", false),
            row("420400", "0I", "3", "100 ", "990", "0", false),
            row("420400", "0K", "3", "100 ", "600", "-1", false),
            row("420400", "0K", "3", "100 ", "990", "-1", false),
            row("420400", "0K", "3", "100 ", "990", "0", false),
            row("420400", "0M", "3", "100 ", "600", "-1", false),
            row("420400", "0M", "3", "100 ", "990", "-1", false),
            row("420400", "0M", "3", "100 ", "990", "0", true),
            row("420400", "0P", "3", "100 ", "600", "-1", false),
            row("420400", "0P", "3", "100 ", "990", "-1", false),
            row("420400", "0P", "3", "100 ", "990", "0", true)
        ) { region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_REGION to region,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("Expenses is zero for $region, $skjema, $kontoklasse, $funksjon, $art, $belop") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder inntektsposteringene ($belop) i driftsregnskapet"
                )
            }
        }
    }
})