package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule145AvskrivningerMotpostAvskrivningerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule145AvskrivningerMotpostAvskrivninger()

        forAll(
            row(
                "all conditions match, Severity.ERROR",
                "420400","0A", 1, 860 , 990, 0, true, Severity.ERROR
            ),
            row(
                "all conditions match, Severity.INFO",
                "420400","0K", 3, 860 , 990, 0, true, Severity.INFO
            ),
            row(
                "isOsloBydel = true",
                "030101","0A", 1, 860 , 990, 0, false, null
            ),
            row(
                "isBevilgningDriftRegnskap = false",
                "420400","0A", 0, 860 , 990, 0, false, null
            ),
            row(
                "funksjon != 860",
                "420400","0A", 1, 861 , 990, 0, false, null
            ),
            row(
                "art != 990",
                "420400","0A", 1, 860 , 991, 0, false, null
            ),
            row(
                "belop != 0",
                "420400","0A", 1, 860 , 990, 1, false, null
            )
      ) { description, region, skjema, kontoklasse, funksjon, art, belop, expectError, expectedSeverity ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_REGION to region,
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_KONTOKLASSE to "$kontoklasse",
                RegnskapConstants.FIELD_FUNKSJON to "$funksjon",
                RegnskapConstants.FIELD_ART to "$art",
                RegnskapConstants.FIELD_BELOP to "$belop"
            ).toKostraRecord().asList()

            When(description) {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = expectedSeverity ?: Severity.INFO,
                    "Korrigér i fila slik at den inneholder motpost avskrivninger ($belop), føres " +
                            "på funksjon 860 og art 990."
                )
            }
        }
    }
})