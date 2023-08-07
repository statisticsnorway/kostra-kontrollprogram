package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.toKostraRecords
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult

class Rule210InterneOverforingerKjopOgSalgTest : BehaviorSpec({
    Given("context") {
        val sut = Rule210InterneOverforingerKjopOgSalg()

        forAll(
            row(
                "matches isBevilgningRegnskap, art, internKjop + internSalg",
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
            row(
                "does not match isBevilgningRegnskap",
                listOf(
                    mapOf(
                        FIELD_SKJEMA to "0X",
                        FIELD_KONTOKLASSE to "3",
                        FIELD_FUNKSJON to "041 ",
                        FIELD_ART to "100",
                        FIELD_BELOP to "0"
                    ),
                    mapOf(
                        FIELD_SKJEMA to "0X",
                        FIELD_KONTOKLASSE to "4",
                        FIELD_FUNKSJON to "045 ",
                        FIELD_ART to "780",
                        FIELD_BELOP to "-1000"
                    )
                ), false
            ),
            row(
                "does not match art",
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
                        FIELD_ART to "100",
                        FIELD_BELOP to "-1000"
                    )
                ), false
            ),
            row(
                "does not match internKjop + internSalg",
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
                        FIELD_BELOP to "0"
                    )
                ), false
            )
        ) { description, recordList, expectError ->
            val kostraRecordList = recordList.toKostraRecords(fieldDefinitions)
            val internKjop = kostraRecordList[0].fieldAsIntOrDefault(FIELD_BELOP)
            val internSalg = kostraRecordList[1].fieldAsIntOrDefault(FIELD_BELOP)
            val internDifferanse = internKjop + internSalg

            When("$description $recordList") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at differansen ($internDifferanse) mellom " +
                            "internkjøp ($internKjop) og internsalg ($internSalg) stemmer overens " +
                            "(margin på +/- 30')"
                )
            }
        }
    }
})