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

class Rule170Funksjon290InvesteringTest : BehaviorSpec({
    Given("context") {
        val sut = Rule170Funksjon290Investering()

        forAll(
            row(
                "matches !it.isOsloBydel, isKommuneRegnskap, isBevilgningInvesteringRegnskap, funksjon, funksjon290Investering",
                "420400", "0A", "0", "290 ", "010", "31", true
            ),
            row(
                "matches !it.isOsloBydel, isKommuneRegnskap, isBevilgningInvesteringRegnskap, funksjon, negative funksjon290Investering",
                "420400", "0A", "0", "290 ", "010", "-31", true
            ),
            row(
                "does not match !it.isOsloBydel",
                "030101", "0A", "0", "290 ", "010", "31", false
            ),
            row(
                "does not match isKommuneRegnskap",
                "420400", "0X", "0", "290 ", "010", "31", false
            ),
            row(
                "does not match isBevilgningInvesteringRegnskap",
                "420400", "0A", "1", "290 ", "010", "31", false
            ),
            row(
                "does not match funksjon",
                "420400", "0A", "0", "291 ", "010", "31", false
            ),
            row(
                "does not match funksjon290Investering +30",
                "420400", "0A", "0", "290 ", "010", "30", false
            ),
            row(
                "does not match funksjon290Investering -30",
                "420400", "0A", "0", "290 ", "010", "-30", false
            )
        ) { description, region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_REGION to region,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("$description for $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at differanse ($belop) på funksjon " +
                            "290 interkommunale samarbeid går i 0 i investeringsregnskapet . (margin på +/- 30')"
                )
            }
        }
    }
})