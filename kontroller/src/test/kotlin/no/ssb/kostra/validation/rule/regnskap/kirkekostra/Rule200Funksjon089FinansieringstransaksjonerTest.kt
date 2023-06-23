package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule200Funksjon089FinansieringstransaksjonerTest : BehaviorSpec({

    Given("context") {
        val sut = Rule200Funksjon089Finansieringstransaksjoner()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row("0F", "3", "089 ", "500", false),
            row("0F", "3", "089 ", "100", true),
        ) { skjema, kontoklasse, funksjon, art, expectedResult ->
            When("For $skjema, $kontoklasse, $funksjon, $art -> $expectedResult") {
                val kostraRecordList = listOf(
                    KostraRecord(
                        index = 0,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = mapOf(
                            FIELD_SKJEMA to skjema,
                            FIELD_KONTOKLASSE to kontoklasse,
                            FIELD_FUNKSJON to funksjon,
                            FIELD_ART to art,
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
                            "Korrigér i fila slik at art ($art) " +
                                    "er gyldig mot funksjon 089. Gyldige arter er 500-580, 830 og 900-980."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})