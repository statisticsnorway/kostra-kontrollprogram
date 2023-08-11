package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils

class Rule115SummeringBalanseAktivaTest : BehaviorSpec({
    val sut = Rule115SummeringBalanseAktiva()

    Given("context") {
        forAll(
            row("XX", "10  ", "1", false), // feil skjema
            row("0B", "30  ", "1", false), // feil kapittel
            row("0B", "10  ", "0", true), // feil belop
            row("0B", "10  ", "1", false), // ok
        ) { skjema, kapittel, belop, expectError ->
            val kostraRecordList = mapOf(
                RegnskapConstants.FIELD_SKJEMA to skjema,
                RegnskapConstants.FIELD_KAPITTEL to kapittel,
                RegnskapConstants.FIELD_BELOP to belop
            ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()

            When("$skjema, $kapittel, $belop") {
                TestUtils.verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder registrering av aktiva/eiendeler " +
                            "($belop), sum sektor 000-990 for kapittel 10-29 i balanse."
                )
            }
        }
    }
})