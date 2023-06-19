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


class Rule110SummeringDriftDifferanseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule110SummeringDriftDifferanse()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.getFieldDefinitions()
            .associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1"
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
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
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
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1000"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0C",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0I",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0K",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
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
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1000"
                    )
                ), true
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
                        FIELD_BELOP to "-1"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "590",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0P",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "600",
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
                val driftUtgifter = kostraRecordList[0].getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                val driftInntekter = kostraRecordList[1].getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                val driftDifferanse = kostraRecordList.sumOf {
                    it.getFieldAsIntegerDefaultEquals0(FIELD_BELOP)
                }

                val validationReportEntries = sut.validate(kostraRecordList)
                val result = validationReportEntries?.any()

                Then("expected result should be equal to $expectedResult") {
                    result?.shouldBeEqual(expectedResult)

                    if (result == true) {
                        validationReportEntries[0].severity.shouldBeEqual(Severity.ERROR)
                        validationReportEntries[0].messageText.shouldBeEqual(
                            "Korrigér differansen ($driftDifferanse) mellom inntekter " +
                                    "($driftInntekter) og utgifter ($driftUtgifter) i driftsregnskapet"
                        )
                    } else {
                        validationReportEntries.shouldBeNull()
                    }
                }
            }
        }
    }
})