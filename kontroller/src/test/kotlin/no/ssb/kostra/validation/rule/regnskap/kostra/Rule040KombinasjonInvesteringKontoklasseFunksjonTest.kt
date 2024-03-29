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
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest

class Rule040KombinasjonInvesteringKontoklasseFunksjonTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule040KombinasjonInvesteringKontoklasseFunksjon(listOf("800")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                "isBevilgningInvesteringRegnskap = true, funksjon match, belop match",
                kostraRecordsInTest("0", "800", "1"),
                expectedErrorMessage = "Korrigér ugyldig funksjon '800' i " +
                        "investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller " +
                        "overfør posteringen til driftsregnskapet.",
            ),
            ForAllRowItem(
                "isBevilgningInvesteringRegnskap = false, funksjon match, belop match",
                kostraRecordsInTest("1", "800", "1"),
            ),
            ForAllRowItem(
                "isBevilgningInvesteringRegnskap = true, funksjon mismatch, belop match",
                kostraRecordsInTest("0", "801", "1"),
            ),
            ForAllRowItem(
                "isBevilgningInvesteringRegnskap = true, funksjon match, belop mismatch",
                kostraRecordsInTest("0", "800", "0"),
            )
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