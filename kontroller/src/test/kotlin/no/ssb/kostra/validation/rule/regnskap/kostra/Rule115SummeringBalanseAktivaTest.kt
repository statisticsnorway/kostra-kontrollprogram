package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule115SummeringBalanseAktivaTest : BehaviorSpec({
    Given("context") {
        val sut = Rule115SummeringBalanseAktiva()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row("0B", "2", "10  ", "010", "1", false),
            row("0B", "2", "10  ", "590", "1", false),
            row("0B", "2", "10  ", "590", "0", true),
            row("0D", "2", "10  ", "010", "1", false),
            row("0D", "2", "10  ", "590", "1", false),
            row("0D", "2", "10  ", "590", "0", true),
            row("0J", "5", "10  ", "010", "1", false),
            row("0J", "5", "10  ", "590", "1", false),
            row("0J", "5", "10  ", "590", "0", true),
            row("0L", "5", "10  ", "010", "1", false),
            row("0L", "5", "10  ", "590", "1", false),
            row("0L", "5", "10  ", "590", "0", true),
            row("0N", "5", "10  ", "010", "1", false),
            row("0N", "5", "10  ", "590", "1", false),
            row("0N", "5", "10  ", "590", "0", true),
            row("0Q", "5", "10  ", "010", "1", false),
            row("0Q", "5", "10  ", "590", "1", false),
            row("0Q", "5", "10  ", "590", "0", true),

            ) { skjema, kontoklasse, kapittel, sektor, belop, expectedResult ->
            When("Activa is zero for $skjema, $kontoklasse, $kapittel, $sektor, $belop") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            FIELD_SKJEMA to skjema,
                            FIELD_KONTOKLASSE to kontoklasse,
                            FIELD_FUNKSJON to kapittel,
                            FIELD_SEKTOR to sektor,
                            FIELD_BELOP to belop
                        )
                    )
                )

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér slik at fila inneholder registrering av aktiva/eiendeler ($belop), " +
                                    "sum sektor 000-990 for kapittel 10-29 i balanse."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})