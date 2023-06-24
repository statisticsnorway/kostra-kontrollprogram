package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule210InterneOverforingerKjopOgSalgTest : BehaviorSpec({
    Given("context") {
        val sut = Rule210InterneOverforingerKjopOgSalg()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "380",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "045 ",
                        FIELD_ART to "780",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "100",
                        FIELD_BELOP to "0"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "045 ",
                        FIELD_ART to "780",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
        ) { recordList, expectedResult ->
            When("List is $recordList") {
                val kostraRecordList = recordList
                    .mapIndexed { index, record ->
                        KostraRecord(
                            index = index + 1,
                            fieldDefinitionByName = fieldDefinitionsByName,
                            valuesByName = record
                        )
                    }
                val internKjop = kostraRecordList[0].getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                val internSalg = kostraRecordList[1].getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                val internDifferanse = internKjop + internSalg

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér i fila slik at differansen ($internDifferanse) mellom " +
                                    "internkjøp ($internKjop) og internsalg ($internSalg) stemmer overens " +
                                    "(margin på +/- 30')"
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})