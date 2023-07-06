package no.ssb.kostra.validation.rule.regnskap.kirkekostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule143AvskrivningerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule143Avskrivninger(),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = true, funksjon match, art match, sum belop match #1",
                    listOf(
                        kostraRecordInTest("1", "041 ", "590", "100"),
                        kostraRecordInTest("1", "045 ", "990", "-131")
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differansen (-31) mellom art 590 (100) stemmer " +
                            "overens med art 990 (-131) (margin på +/- 30')"
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = true, funksjon match, art match, sum belop match #2",
                    listOf(
                        kostraRecordInTest("1", "041 ", "590", "-100"),
                        kostraRecordInTest("1", "045 ", "990", "131")
                    ),
                    expectedErrorMessage = "Korrigér i fila slik at differansen (31) mellom art 590 (-100) stemmer " +
                            "overens med art 990 (131) (margin på +/- 30')"
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = false, funksjon match, art match, sum belop match",
                    listOf(
                        kostraRecordInTest("0", "041 ", "590", "100"),
                        kostraRecordInTest("0", "045 ", "990", "-131")
                    )
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = true, funksjon mismatch, art match, sum belop match",
                    listOf(
                        kostraRecordInTest("1", "040 ", "590", "100"),
                        kostraRecordInTest("1", "046 ", "990", "-131")
                    )
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = true, funksjon match, art mismatch, sum belop match",
                    listOf(
                        kostraRecordInTest("1", "041 ", "591", "100"),
                        kostraRecordInTest("1", "045 ", "991", "-131")
                    )
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = true, funksjon match, art match, sum belop mismatch #1",
                    listOf(
                        kostraRecordInTest("1", "041 ", "590", "100"),
                        kostraRecordInTest("1", "045 ", "990", "-130")
                    )
                ),
                ForAllRowItem(
                    "isBevilgningDriftRegnskap = true, funksjon match, art match, sum belop mismatch #2",
                    listOf(
                        kostraRecordInTest("1", "041 ", "590", "100"),
                        kostraRecordInTest("1", "045 ", "990", "-70")
                    )
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordInTest(
            kontoklasse: String,
            funksjon: String,
            art: String,
            belop: String
        ) = mapOf(
            FIELD_REGION to "420400",
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to kontoklasse,
            FIELD_FUNKSJON to funksjon,
            FIELD_ART to art,
            FIELD_BELOP to belop
        ).toKostraRecord()
    }
}