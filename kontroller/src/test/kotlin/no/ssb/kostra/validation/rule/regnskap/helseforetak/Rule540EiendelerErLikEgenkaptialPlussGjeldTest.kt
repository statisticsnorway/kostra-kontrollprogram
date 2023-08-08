package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils

class Rule540EiendelerErLikEgenkaptialPlussGjeldTest : BehaviorSpec({
    val sut = Rule540EiendelerErLikEgenkaptialPlussGjeld()

    Given("context") {
        forAll(
            row("0X", "0", "0", "-500", false), // skjema mismatch -> OK
            row("0Y", "0", "0", "0", false), // belop match -> OK
            row("0Y", "1000", "0", "0", true), // belop mismatch -> Fail
            row("0Y", "0", "-500", "0", true), // belop mismatch -> Fail
            row("0Y", "0", "0", "-500", true), // belop mismatch -> Fail
            row("0Y", "1000", "-500", "0", true), // belop mismatch -> Fail
            row("0Y", "1000", "0", "-500", true), // belop mismatch -> Fail
            row("0Y", "1000", "-500", "-500", false), // belop match -> OK
        ) { skjema, sumEiendeler, sumEgenkapital, sumGjeld, expectError ->
            val kostraRecordList = listOf(
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_ART to "100",
                    RegnskapConstants.FIELD_BELOP to sumEiendeler
                ),
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_ART to "200",
                    RegnskapConstants.FIELD_BELOP to sumEgenkapital
                ),
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_ART to "210",
                    RegnskapConstants.FIELD_BELOP to sumGjeld
                ),
            ).toKostraRecords(fieldDefinitions)
            When("$skjema, $sumEiendeler, $sumEgenkapital, $sumGjeld") {
                val sumBalanse = sumEiendeler.toInt() + (sumEgenkapital.toInt() + sumGjeld.toInt())
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.WARNING,
                    "Balansen ($sumBalanse) skal balansere ved at sum eiendeler ($sumEiendeler)  = sum egenkapital ($sumEgenkapital) + sum gjeld ($sumGjeld) . Differanser +/- 50' kroner godtas"
                )
            }
        }
    }
})
/*
* "Balansen (-1000) skal balansere ved at sum eiendeler (0)  = sum egenkapital (-1000) + sum gjeld (0) . Differanser +/- 50' kroner godtas" should start with
* "Balansen (0-10000) skal balansere ved at sum eiendeler (0)  = sum egenkapital (-1000) + sum gjeld (0) . Differanser +/- 50' kroner godtas" (diverged at index 10)

*
* */