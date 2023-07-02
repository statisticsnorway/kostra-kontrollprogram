package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.TestUtils.verifyValidationResult
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecords

class Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekterTest : BehaviorSpec({
    Given("context") {
        val sut = Rule215InterneOverforingerKalkulatoriskeUtgifterOgInntekter()

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
            )
        ) { recordList, expectError ->
            val kostraRecordList = recordList.toKostraRecords()
            val kalkulatoriskeUtgifter = kostraRecordList[0].getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP)
            val kalkulatoriskeInntekter = kostraRecordList[1].getFieldAsIntegerOrDefault(RegnskapConstants.FIELD_BELOP)
            val kalkulatoriskeDifferanse = kalkulatoriskeUtgifter + kalkulatoriskeInntekter

            When("List is $recordList") {
                verifyValidationResult(
                    validationReportEntries = sut.validate(kostraRecordList),
                    expectError = expectError,
                    expectedSeverity = Severity.ERROR,
                    "Korrigér i fila slik at differansen ($kalkulatoriskeDifferanse) mellom " +
                            "kalkulatoriske utgifter ($kalkulatoriskeUtgifter) og inntekter " +
                            "($kalkulatoriskeInntekter) ved kommunal tjenesteytelse stemmer overens " +
                            "(margin på +/- 30')"
                )
            }
        }
    }
})