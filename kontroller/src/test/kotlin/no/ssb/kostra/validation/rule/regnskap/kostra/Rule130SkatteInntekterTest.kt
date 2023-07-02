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
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule130SkatteInntekterTest : BehaviorSpec({
    Given("context") {
        val sut = Rule130SkatteInntekter()

        forAll(
            row(
                "matches !(isOsloBydel || it.isLongyearbyen), isRegional, isBevilgningDriftRegnskap, funksjon = 800, art = 870",
                "420400", "0A", "1", "800 ", "870", "0", true
            ),
            row(
                "does not match region, Oslo",
                "030101", "0A", "1", "800 ", "870", "0", false
            ),
            row(
                "does not match region, Longyearbyen",
                "211100", "0A", "1", "800 ", "870", "0", false
            ),
            row(
                "does not match isRegional",
                "420400", "0I", "1", "800 ", "870", "0", false
            ),
            row(
                "does not match isBevilgningDriftRegnskap",
                "420400", "0A", "0", "800 ", "870", "0", false
            ),
            row(
                "does not match funksjon",
                "420400", "0A", "1", "801 ", "870", "0", true
            ),
            row(
                "does not match art",
                "420400", "0A", "1", "800 ", "871", "0", true
            ),
            row(
                "does not match belop",
                "420400", "0A", "1", "800 ", "870", "-1", false
            )
        ) { description, region, skjema, kontoklasse, funksjon, art, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_REGION to region,
                FIELD_SKJEMA to skjema,
                FIELD_KONTOKLASSE to kontoklasse,
                FIELD_FUNKSJON to funksjon,
                FIELD_ART to art,
                FIELD_BELOP to belop
            ).toKostraRecord().asList()

            When("$description for $region, $skjema, $kontoklasse, $funksjon, $art, $belop -> $expectError") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder skatteinntekter ($belop)."
                )
            }
        }
    }
})