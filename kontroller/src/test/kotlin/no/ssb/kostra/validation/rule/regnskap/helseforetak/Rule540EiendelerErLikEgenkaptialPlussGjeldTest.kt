package no.ssb.kostra.validation.rule.regnskap.helseforetak

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule540EiendelerErLikEgenkaptialPlussGjeldTest : BehaviorSpec({
    val sut = Rule540EiendelerErLikEgenkaptialPlussGjeld()

    Given("context") {
        forAll(
            row("0X", "0", "0", "-1000", false), // skjema mismatch -> OK
            row("0Y", "0", "-1000", "0", true), // belop mismatch -> OK
            row("0Y", "1000", "-1000", "0", false), // skjema mismatch -> OK
        ) { skjema, sumEiendeler, sumEgenkapital, sumGjeld, expectError ->
            val kostraRecordList = listOf(
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_ART to "100",
                    RegnskapConstants.FIELD_BELOP to sumEiendeler
                ).toKostraRecord(),
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_ART to "200",
                    RegnskapConstants.FIELD_BELOP to sumEgenkapital
                ).toKostraRecord(),
                mapOf(
                    RegnskapConstants.FIELD_SKJEMA to skjema,
                    RegnskapConstants.FIELD_ART to "210",
                    RegnskapConstants.FIELD_BELOP to sumGjeld
                ).toKostraRecord(),
            )
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