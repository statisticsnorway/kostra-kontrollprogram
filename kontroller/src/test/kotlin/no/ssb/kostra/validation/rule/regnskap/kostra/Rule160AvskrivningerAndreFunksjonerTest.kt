package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_REGION
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest

class Rule160AvskrivningerAndreFunksjonerTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule160AvskrivningerAndreFunksjoner(),
            forAllRows = listOf(
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon match #1, art match, belop match",
                    kostraRecordsInTest("420400", "1", "800 ", "590", belop = "1"),
                    expectedErrorMessage = "Korrigér i fila slik at avskrivningene (1) føres på " +
                            "tjenestefunksjon og ikke på funksjonene ([800])",
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon match #2, art match, belop match",
                    kostraRecordsInTest("420400", "1", "899 ", "590", belop = "1"),
                    expectedErrorMessage = "Korrigér i fila slik at avskrivningene (1) føres på " +
                            "tjenestefunksjon og ikke på funksjonene ([899])",
                ),
                ForAllRowItem(
                    "region = Oslo, isBevilgningDriftRegnskap = true, funksjon match, art match, belop match",
                    kostraRecordsInTest("030101", "1", "800 ", "590", belop = "1"),
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = false, funksjon match, art match, belop match",
                    kostraRecordsInTest("420400", "0", "800 ", "590", belop = "1"),
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon mismatch #1, art match, belop match",
                    kostraRecordsInTest("420400", "1", "799 ", "590", belop = "1"),
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon mismatch #2, art match, belop match",
                    kostraRecordsInTest("420400", "1", "900 ", "590", belop = "1"),
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon match, art mismatch, belop match",
                    kostraRecordsInTest("420400", "1", "800 ", "591", belop = "1"),
                ),
                ForAllRowItem(
                    "region != Oslo, isBevilgningDriftRegnskap = true, funksjon match, art match, belop mismatch",
                    kostraRecordsInTest("420400", "1", "800 ", "590", belop = "0"),
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
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}