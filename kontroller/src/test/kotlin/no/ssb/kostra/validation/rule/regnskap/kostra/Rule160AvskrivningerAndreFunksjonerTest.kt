package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule160AvskrivningerAndreFunksjonerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule160AvskrivningerAndreFunksjoner(),
            forAllRows = listOf(
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon matching #1, art matching, belop matching",
                    kostraRecordsInTest("420400","1", "800 ", "590", belop = "1"),
                    expectedErrorMessage = "Korrigér i fila slik at avskrivningene (1) føres på " +
                            "tjenestefunksjon og ikke på funksjonene ([800])"
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon matching #2, art matching, belop matching",
                    kostraRecordsInTest("420400","1", "899 ", "590", belop = "1"),
                    expectedErrorMessage = "Korrigér i fila slik at avskrivningene (1) føres på " +
                            "tjenestefunksjon og ikke på funksjonene ([899])"
                ),
                ForAllRowItem(
                    "region = Oslo, isBevilgningDriftRegnskap = true, funksjon matching, art matching, belop matching",
                    kostraRecordsInTest("030101","1", "800 ", "590", belop = "1")
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = false, funksjon matching, art matching, belop matching",
                    kostraRecordsInTest("420400","0", "800 ", "590", belop = "1")
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon not matching #1, art matching, belop matching",
                    kostraRecordsInTest("420400","1", "799 ", "590", belop = "1")
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon not matching #2, art matching, belop matching",
                    kostraRecordsInTest("420400","1", "900 ", "590", belop = "1")
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon matching, art not matching, belop matching",
                    kostraRecordsInTest("420400","1", "800 ", "591", belop = "1")
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon matching, art matching, belop not matching",
                    kostraRecordsInTest("420400","1", "800 ", "590", belop = "0")
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            region: String,
            kontoklasse: String,
            funksjon: String,
            art: String,
            belop: String
        ): List<KostraRecord> = mapOf(
            FIELD_REGION to region,
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to kontoklasse,
            FIELD_FUNKSJON to funksjon,
            FIELD_ART to art,
            FIELD_BELOP to belop
        ).toKostraRecord().asList()
    }
}