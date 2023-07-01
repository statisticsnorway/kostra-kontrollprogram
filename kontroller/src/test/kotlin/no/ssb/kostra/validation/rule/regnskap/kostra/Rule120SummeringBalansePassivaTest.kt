package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule120SummeringBalansePassivaTest : BehaviorSpec({
    Given("context") {
        val sut = Rule120SummeringBalansePassiva()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row("0B", "2", "31  ", "010", "1", false),
            row("0B", "2", "31  ", "590", "1", false),
            row("0B", "2", "31  ", "590", "0", true),
            row("0D", "2", "31  ", "010", "1", false),
            row("0D", "2", "31  ", "590", "1", false),
            row("0D", "2", "31  ", "590", "0", true),
            row("0J", "5", "31  ", "010", "1", false),
            row("0J", "5", "31  ", "590", "1", false),
            row("0J", "5", "31  ", "590", "0", true),
            row("0L", "5", "31  ", "010", "1", false),
            row("0L", "5", "31  ", "590", "1", false),
            row("0L", "5", "31  ", "590", "0", true),
            row("0N", "5", "31  ", "010", "1", false),
            row("0N", "5", "31  ", "590", "1", false),
            row("0N", "5", "31  ", "590", "0", true),
            row("0Q", "5", "31  ", "010", "1", false),
            row("0Q", "5", "31  ", "590", "1", false),
            row("0Q", "5", "31  ", "590", "0", true)
        ) { skjema, kontoklasse, kapittel, sektor, belop, expectError ->
            val kostraRecordList = listOf(
                KostraRecord(
                    fieldDefinitionByName = fieldDefinitionsByName,
                    valuesByName = mapOf(
                        RegnskapConstants.FIELD_SKJEMA to skjema,
                        RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
                        RegnskapConstants.FIELD_FUNKSJON to kapittel,
                        RegnskapConstants.FIELD_SEKTOR to sektor,
                        RegnskapConstants.FIELD_BELOP to belop
                    )
                )
            )

            When("Activa is zero for $skjema, $kontoklasse, $kapittel, $sektor, $belop") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér slik at fila inneholder registrering av passiva/gjeld og egenkapital " +
                            "($belop), sum sektor 000-990 for kapittel 31-5990 i balanse."
                )
            }
        }
    }
})