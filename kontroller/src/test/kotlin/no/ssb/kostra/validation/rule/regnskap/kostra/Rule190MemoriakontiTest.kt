package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KAPITTEL
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SEKTOR
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule190MemoriakontiTest : BehaviorSpec({
    Given("context") {
        val sut = Rule190Memoriakonti()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions
            .associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0B",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0B",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0B",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0B",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0D",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0D",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0D",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0D",
                        FIELD_KONTOKLASSE to "2",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0J",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0J",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0J",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0J",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0L",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0L",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0L",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0L",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0N",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0N",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0N",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0N",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0Q",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0Q",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0Q",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9100",
                        FIELD_SEKTOR to "000",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0Q",
                        FIELD_KONTOKLASSE to "5",
                        FIELD_KAPITTEL to "9999",
                        FIELD_SEKTOR to "990",
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
                val memoriakonti =
                    kostraRecordList[0].getFieldAsIntegerOrDefault(FIELD_BELOP)
                val motpostMemoriakonti =
                    kostraRecordList[1].getFieldAsIntegerOrDefault(FIELD_BELOP)

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.WARNING)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér i fila slik at differansen (${memoriakonti + motpostMemoriakonti}) " +
                                    "mellom memoriakontiene ($memoriakonti) og motkonto for memoriakontiene " +
                                    "($motpostMemoriakonti) går i 0. (margin på +/- 10')"
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})
