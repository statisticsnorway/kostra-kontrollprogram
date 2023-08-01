package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils

class Rule520Konti19XKunPositiveBelopTest : BehaviorSpec({
    val sut = Rule520Konti19XKunPositiveBelop(listOf("190"))

    Given("context") {
        forAll(
            row("0X", "100", "0", false), // skjema mismatch -> OK
            row("0Y", "100", "0", false), // art mismatch -> OK
            row("0Y", "190", "0", true), // orgnr mismatch -> Fail
            row("0Y", "190", "1", false), // OK
        ) { skjema, art, belop, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_ART to art,
                RegnskapConstants.FIELD_BELOP to belop,
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("$skjema, $art, $belop") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Kun positive beløp er gyldig. Fant ugyldig beløp (${
                        kostraRecordList[0].fieldAsIntOrDefault(
                            RegnskapConstants.FIELD_BELOP
                        )
                    })"
                )
            }
        }
    }
})