package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils


class Rule530SummeringDifferanseTest : BehaviorSpec({
    val sut = Rule530SummeringDifferanse()

    Given("context") {
        forAll(
            row("0Y", "0", "0", false), // skjema mismatch -> OK
            row("0X", "0", "1000", true), // belop mismatch -> OK
            row("0X", "0", "-1000", true), // belop mismatch -> OK
            row("0X", "1000", "-1000", false), // belop ok -> OK
        ) { skjema, belop1, belop2, expectError ->
            val kostraRecordList = listOf(
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_BELOP to belop1
                ),
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_BELOP to belop2
                )
            ).toKostraRecords(fieldDefinitions)
            When("$skjema, $belop1, $belop2") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Sjekk at sum art 300 til og med art 899 skal være 0, her ($belop2). Differanse +/- 100' kroner godtas."
                )
            }
        }
    }
})