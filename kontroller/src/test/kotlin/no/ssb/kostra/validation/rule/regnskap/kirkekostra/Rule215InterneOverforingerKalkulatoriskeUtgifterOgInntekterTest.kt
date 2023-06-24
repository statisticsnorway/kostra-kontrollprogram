package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekterTest : BehaviorSpec({
    Given("context") {
        val sut = Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekter()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to "0F",
                        RegnskapConstants.FIELD_KONTOKLASSE to "3",
                        RegnskapConstants.FIELD_FUNKSJON to "041 ",
                        RegnskapConstants.FIELD_ART to "390",
                        RegnskapConstants.FIELD_BELOP to "100"
                    ),
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to "0F",
                        RegnskapConstants.FIELD_KONTOKLASSE to "4",
                        RegnskapConstants.FIELD_FUNKSJON to "045 ",
                        RegnskapConstants.FIELD_ART to "790",
                        RegnskapConstants.FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to "0F",
                        RegnskapConstants.FIELD_KONTOKLASSE to "3",
                        RegnskapConstants.FIELD_FUNKSJON to "041 ",
                        RegnskapConstants.FIELD_ART to "100",
                        RegnskapConstants.FIELD_BELOP to "0"
                    ),
                    mapOf(
                        RegnskapConstants.FIELD_SKJEMA to "0F",
                        RegnskapConstants.FIELD_KONTOKLASSE to "4",
                        RegnskapConstants.FIELD_FUNKSJON to "045 ",
                        RegnskapConstants.FIELD_ART to "790",
                        RegnskapConstants.FIELD_BELOP to "-1000"
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
                val kalkulatoriskeUtgifter = kostraRecordList[0].getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP)
                val kalkulatoriskeInntekter = kostraRecordList[1].getFieldAsIntegerDefaultEquals0(RegnskapConstants.FIELD_BELOP)
                val kalkulatoriskeDifferanse = kalkulatoriskeUtgifter + kalkulatoriskeInntekter

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér i fila slik at differansen ($kalkulatoriskeDifferanse) mellom " +
                                    "kalkulatoriske utgifter ($kalkulatoriskeUtgifter) og inntekter " +
                                    "($kalkulatoriskeInntekter) ved kommunal tjenesteytelse stemmer overens " +
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