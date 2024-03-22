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
import no.ssb.kostra.validation.rule.RuleTestData

class Rule040KombinasjonInvesteringKontoklasseFunksjonTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule040KombinasjonInvesteringKontoklasseFunksjon(listOf("800")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                description = "annually, isBevilgningInvesteringRegnskap = true, funksjon match, belop match",
                context = kostraRecordsInTest("0", "800", "1"),
                expectedErrorMessage = "Korrigér ugyldig funksjon '800' i " +
                        "investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller " +
                        "overfør posteringen til driftsregnskapet.",
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "annually, isBevilgningInvesteringRegnskap = false, funksjon match, belop match",
                context = kostraRecordsInTest("1", "800", "1"),
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "annually, isBevilgningInvesteringRegnskap = true, funksjon mismatch, belop match",
                context = kostraRecordsInTest("0", "801", "1"),
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "annually, isBevilgningInvesteringRegnskap = true, funksjon match, belop mismatch",
                context = kostraRecordsInTest("0", "800", "0"),
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "quarterly, isBevilgningInvesteringRegnskap = true, funksjon match, belop match",
                context = kostraRecordsInTest("0", "800", "1"),
                expectedErrorMessage = "Korrigér ugyldig funksjon '800' i " +
                        "investeringsregnskapet til en gyldig funksjon i investeringsregnskapet eller " +
                        "overfør posteringen til driftsregnskapet.",
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            ),
            ForAllRowItem(
                description = "quarterly, isBevilgningInvesteringRegnskap = false, funksjon match, belop match",
                context = kostraRecordsInTest("1", "800", "1"),
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            ),
            ForAllRowItem(
                description = "quarterly, isBevilgningInvesteringRegnskap = true, funksjon mismatch, belop match",
                context = kostraRecordsInTest("0", "801", "1"),
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            ),
            ForAllRowItem(
                description = "quarterly, isBevilgningInvesteringRegnskap = true, funksjon match, belop mismatch",
                context = kostraRecordsInTest("0", "800", "0"),
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
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

        private fun kostraArguments(kvartal: String) = RuleTestData.argumentsInTest.copy(kvartal = kvartal)
    }
}