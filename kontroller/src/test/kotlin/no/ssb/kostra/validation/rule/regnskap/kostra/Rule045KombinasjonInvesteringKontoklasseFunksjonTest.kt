package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory

class Rule045KombinasjonInvesteringKontoklasseFunksjonTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule045KombinasjonInvesteringKontoklasseFunksjon(listOf("100")),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = true, funksjon match, belop match",
                    kostraRecordsInTest("0", "100", "1"),
                    expectedErrorMessage = "Kun advarsel, hindrer ikke innsending: (100) regnes å være ulogisk " +
                            "funksjon i investeringsregnskapet. Vennligst vurder å postere på annen funksjon " +
                            "eller om posteringen hører til i driftsregnskapet."
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = false, funksjon match, belop match",
                    kostraRecordsInTest("1", "100", "1")
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = true, funksjon mismatch, belop match",
                    kostraRecordsInTest("0", "101", "1")
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = true, funksjon match, belop mismatch",
                    kostraRecordsInTest("0", "100", "0")
                )
            ),
            expectedSeverity = Severity.INFO,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: String,
            funksjon: String,
            belop: String
        ): List<KostraRecord> = mapOf(
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to kontoklasse,
            FIELD_FUNKSJON to funksjon,
            FIELD_BELOP to belop,
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}