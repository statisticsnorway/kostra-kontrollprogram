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
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule126SummeringDriftOsloInternDifferanseTest : BehaviorSpec({
    Given("context") {
        val sut = Rule126SummeringDriftOsloInternDifferanse()

        forAll(
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "0"
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
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
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
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "0"
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
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0A",
                        FIELD_KONTOKLASSE to "1",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1000"
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
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "030100",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-100"
                    )
                ), false
                /** TODO Jon Ole: Denne gir ikke feil */
            ),
            row(
                listOf(
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "030101",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
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
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "298",
                        FIELD_BELOP to "100"
                    ),
                    mapOf(
                        FIELD_REGION to "420400",
                        FIELD_SKJEMA to "0M",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "100 ",
                        FIELD_ART to "798",
                        FIELD_BELOP to "-1000"
                    )
                ), false
            )
        ) { recordList, expectError ->
            val kostraRecordList = recordList.toKostraRecords(RegnskapFieldDefinitions.fieldDefinitions)
            val sumArt298Drift = kostraRecordList[0].fieldAsIntOrDefault(FIELD_BELOP)
            val sumArt798Drift = kostraRecordList[1].fieldAsIntOrDefault(FIELD_BELOP)
            val sumOslointerneDrift = kostraRecordList.sumOf { it.fieldAsIntOrDefault(FIELD_BELOP) }

            When("List is $recordList") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér differansen ($sumOslointerneDrift) mellom sum over alle funksjoner " +
                            "for art 298 ($sumArt298Drift) og sum over alle funksjoner for art 798 " +
                            "($sumArt798Drift) i driftsregnskapet."
                )
            }
        }
    }
})
