package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule143AvskrivningerTest : BehaviorSpec({
    Given("context") {
        val sut = Rule143Avskrivninger()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "0"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0F",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            )
        ) { recordList, expectError ->
            val kostraRecordList = recordList
                .mapIndexed { index, record ->
                    KostraRecord(
                        lineNumber = index + 1,
                        fieldDefinitionByName = fieldDefinitionsByName,
                        valuesByName = record
                    )
                }
            val avskrivninger = kostraRecordList[0].getFieldAsIntegerOrDefault(FIELD_BELOP)
            val motpostAvskrivninger = kostraRecordList[1].getFieldAsIntegerOrDefault(FIELD_BELOP)
            val differanse = avskrivninger + motpostAvskrivninger

            When("List is $recordList") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at differansen ($differanse) " +
                            "mellom art 590 ($avskrivninger) stemmer overens med art " +
                            "990 ($motpostAvskrivninger) (margin på +/- 30')"
                )
            }
        }
    }
})