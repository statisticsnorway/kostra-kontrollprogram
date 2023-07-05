package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.program.KostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule045KombinasjonInvesteringKontoklasseFunksjonTest : BehaviorSpec({
    include(
        KostraTestFactory.validationRuleTest(
            sut = Rule045KombinasjonInvesteringKontoklasseFunksjon(listOf("100")),
            forAllRows = listOf(
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = true, funksjon matching, belop matching",
                    kostraRecordsInTest("0", "100", "1"),
                    expectedErrorMessage = "Kun advarsel, hindrer ikke innsending: (100) regnes å være ulogisk " +
                            "funksjon i investeringsregnskapet. Vennligst vurder å postere på annen funksjon " +
                            "eller om posteringen hører til i driftsregnskapet."
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = false, funksjon matching, belop matching",
                    kostraRecordsInTest("1", "100", "1")
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = true, funksjon not matching, belop matching",
                    kostraRecordsInTest("0", "101", "1")
                ),
                ForAllRowItem(
                    "isBevilgningInvesteringRegnskap = true, funksjon matching, belop not matching",
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
            RegnskapConstants.FIELD_SKJEMA to "0A",
            RegnskapConstants.FIELD_KONTOKLASSE to kontoklasse,
            RegnskapConstants.FIELD_FUNKSJON to funksjon,
            RegnskapConstants.FIELD_BELOP to belop,
        ).toKostraRecord().asList()
    }
}