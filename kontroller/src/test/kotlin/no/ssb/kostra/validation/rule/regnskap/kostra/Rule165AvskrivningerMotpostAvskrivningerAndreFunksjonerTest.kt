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
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule165AvskrivningerMotpostAvskrivningerAndreFunksjonerTest : BehaviorSpec({
    val sut = Rule165AvskrivningerMotpostAvskrivningerAndreFunksjoner()

    Given("context") {
        forAll(
            row(
                "matches !it.isOsloBydel(), isBevilgningDriftRegnskap, funksjon, art, belop",
                "420400", "0A", "1", "100 ", "990", "1", true
            ),
            row(
                "does not match !it.isOsloBydel()",
                "030101", "0A", "1", "100 ", "990", "1", false
            ),
            row(
                "does not match isBevilgningDriftRegnskap",
                "420400", "0A", "0", "100 ", "990", "1", false
            ),
            row(
                "does not match funksjon",
                "420400", "0A", "1", "860 ", "990", "1", false
            ),
            row(
                "does not match art",
                "420400", "0A", "1", "100 ", "991", "1", false
            ),
            row(
                "does not match belop",
                "420400", "0A", "1", "100 ", "990", "0", false
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
                    "Korrigér i fila slik at motpost avskrivninger ($belop) kun er " +
                            "ført på funksjon 860, art 990 og ikke på funksjonene ([${funksjon.trim()}])"
                )
            }
        }
    }

    Given("two records that balances to zero amount") {
        forAll(
            row(
                "sum belop = 0",
                listOf(-1, 1),
                false
            ),
            row(
                "sum belop = -1",
                listOf(-2, 1),
                true
            ),
            row(
                "sum belop = 1",
                listOf(-1, 2),
                true
            )
        ) { description, belop, expectError ->
            val kostraRecords = belop.map {
                mapOf(
                    FIELD_REGION to "420400",
                    FIELD_SKJEMA to "0A",
                    FIELD_KONTOKLASSE to "1",
                    FIELD_FUNKSJON to "100",
                    FIELD_ART to "990",
                    FIELD_BELOP to it.toString()
                )
            }.toKostraRecords(fieldDefinitions)

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecords, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at motpost avskrivninger (${belop.sum()}) kun er ført " +
                            "på funksjon 860, art 990 og ikke på funksjonene ([100])"
                )
            }
        }
    }
})