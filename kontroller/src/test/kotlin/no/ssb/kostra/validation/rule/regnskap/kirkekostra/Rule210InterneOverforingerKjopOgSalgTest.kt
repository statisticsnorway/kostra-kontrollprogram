package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecords

class Rule210InterneOverforingerKjopOgSalgTest : BehaviorSpec({
    Given("context") {
        val sut = Rule210InterneOverforingerKjopOgSalg()

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
            )
        ) { recordList, expectError ->
            val kostraRecordList = recordList.toKostraRecords()
            val internKjop = kostraRecordList[0].getFieldAsIntegerOrDefault(FIELD_BELOP)
            val internSalg = kostraRecordList[1].getFieldAsIntegerOrDefault(FIELD_BELOP)
            val internDifferanse = internKjop + internSalg

            When("List is $recordList") {
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