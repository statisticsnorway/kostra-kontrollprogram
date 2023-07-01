package no.ssb.kostra.validation.rule.regnskap.kostra

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

class Rule127SummeringInvesteringOsloInternDifferanseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule127SummeringInvesteringOsloInternDifferanse()
        val fieldDefinitionsByName = RegnskapFieldDefinitions.fieldDefinitions.associateBy { it.name }

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "0"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "0",
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
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-100"
                    )
                ), false
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "0"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "0",
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
                        FIELD_KONTOKLASSE to "0",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "0",
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
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1"
                    )
                ), false /** TODO Jon Ole: Denne gir ikke feil */
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "4",
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
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "1"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-10"
                    )
                ), false
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
            val sumArt298Investering = kostraRecordList[0].getFieldAsIntegerOrDefault(FIELD_BELOP)
            val sumArt798Investering = kostraRecordList[1].getFieldAsIntegerOrDefault(FIELD_BELOP)
            val sumOslointerneInvestering = kostraRecordList.sumOf { it.getFieldAsIntegerOrDefault(FIELD_BELOP) }

            When("List is $recordList") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér differansen ($sumOslointerneInvestering) mellom sum over alle funksjoner " +
                            "for art 298 ($sumArt298Investering) og sum over alle funksjoner for art 798 " +
                            "($sumArt798Investering) i investeringsregnskapet."
                )
            }
        }
    }
})
