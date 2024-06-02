package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_KONTOKLASSE
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleNoArgsTest
import no.ssb.kostra.validation.rule.RuleTestData

class Rule020KombinasjonDriftKontoklasseFunksjonTest : BehaviorSpec({
    include(
        validationRuleNoArgsTest(
            sut = Rule020KombinasjonDriftKontoklasseFunksjon(listOf("841")),
            expectedSeverity = Severity.ERROR,
            ForAllRowItem(
                description = "annually, all conditions match",
                context = kostraRecordsInTest(1, 841, 1),
                expectedErrorMessage = "Korrigér ugyldig funksjon '841' i driftsregnskapet " +
                        "til en gyldig funksjon i driftsregnskapet eller overfør posteringen til " +
                        "investeringsregnskapet.",
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "annually, isBevilgningDriftRegnskap = false",
                context = kostraRecordsInTest(0, 841, 1),
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "annually, funksjon != 841",
                context = kostraRecordsInTest(1, 842, 1),
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "annually, belop == 0",
                context = kostraRecordsInTest(1, 841, 0),
                arguments = kostraArguments(" "),
            ),
            ForAllRowItem(
                description = "quarterly, all conditions match",
                context = kostraRecordsInTest(1, 841, 1),
                expectedErrorMessage = "Korrigér ugyldig funksjon '841' i driftsregnskapet " +
                        "til en gyldig funksjon i driftsregnskapet eller overfør posteringen til " +
                        "investeringsregnskapet.",
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            ),
            ForAllRowItem(
                description = "quarterly, isBevilgningDriftRegnskap = false",
                context = kostraRecordsInTest(0, 841, 1),
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            ),
            ForAllRowItem(
                description = "quarterly, funksjon != 841",
                context = kostraRecordsInTest(1, 842, 1),
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            ),
            ForAllRowItem(
                description = "quarterly, belop == 0",
                context = kostraRecordsInTest(1, 841, 0),
                arguments = kostraArguments("1"),
                expectedSeverity = Severity.WARNING,
            )
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            kontoklasse: Int,
            funksjon: Int,
            belop: Int
        ) = mapOf(
            FIELD_SKJEMA to "0A",
            FIELD_KONTOKLASSE to "$kontoklasse",
            FIELD_FUNKSJON to "$funksjon",
            FIELD_BELOP to "$belop"
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()

        private fun kostraArguments(kvartal: String) = RuleTestData.argumentsInTest.copy(kvartal = kvartal)
    }
}