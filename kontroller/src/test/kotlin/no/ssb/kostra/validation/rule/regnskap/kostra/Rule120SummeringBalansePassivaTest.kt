package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.RuleTestData.argumentsInTest
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule120SummeringBalansePassivaTest : BehaviorSpec({
    Given("context") {
        val sut = Rule120SummeringBalansePassiva()

        forAll(
            row("XX", "30  ", "1", false), // feil skjema
            row("0B", "10  ", "1", false), // feil kapittel
            row("0B", "30  ", "0", true), // feil belop
            row("0B", "30  ", "1", false), // ok
        ) { skjema, kapittel, belop, expectError ->
            val kostraRecordList = mapOf(
                FIELD_SKJEMA to skjema,
                FIELD_FUNKSJON to kapittel,
                FIELD_BELOP to belop
            ).toKostraRecord(1, fieldDefinitions).asList()

            When("Activa is zero for $skjema, $kapittel, $belop") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList, argumentsInTest),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital " +
                            "($belop), sum sektor 000-990 for kapittel 31-5990 i balanse."
                )
            }
        }
    }
})