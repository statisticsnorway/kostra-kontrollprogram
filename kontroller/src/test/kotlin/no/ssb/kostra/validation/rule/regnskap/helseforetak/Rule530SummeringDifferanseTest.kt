package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule530SummeringDifferanseTest : BehaviorSpec({
    val sut = Rule530SummeringDifferanse()

    Given("context") {
        forAll(
            row("0Y", "0", "0", false), // skjema mismatch -> OK
            row("0X", "0", "-1000", true), // belop mismatch -> OK
            row("0X", "1000", "-1000", false), // skjema mismatch -> OK
        ) { skjema, belop1, belop2, expectError ->
            val kostraRecordList = listOf(
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_BELOP to belop1
                ).toKostraRecord(),
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_BELOP to belop2
                ).toKostraRecord(),
            )
            When("$skjema, $belop1, $belop2") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Sjekk at sum art 300 til og med art 899 skal være 0, her ($belop2). Differanse +/- 100' kroner godtas."
                )
            }
        }
    }
})