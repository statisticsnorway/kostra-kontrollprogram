package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity

class Rule126SummeringDriftOsloInternDifferanseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule126SummeringDriftOsloInternDifferanse()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-10"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-10"
                    )
                ), false
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
                val sumArt298Drift = kostraRecordList[0].getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                val sumArt798Drift = kostraRecordList[1].getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                val sumOslointerneDrift = kostraRecordList.sumOf {
                    it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                }

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér differansen ($sumOslointerneDrift) mellom sum over alle funksjoner " +
                                    "for art 298 ($sumArt298Drift) og sum over alle funksjoner for art 798 " +
                                    "($sumArt798Drift) i driftsregnskapet."
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})
