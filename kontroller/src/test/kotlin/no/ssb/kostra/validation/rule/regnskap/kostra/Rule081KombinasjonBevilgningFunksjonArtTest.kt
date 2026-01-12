package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory
import no.ssb.kostra.validation.rule.RuleTestData

class Rule081KombinasjonBevilgningFunksjonArtTest :
    BehaviorSpec({
        include(
            KostraTestFactory.validationRuleNoArgsTest(
                sut = Rule081KombinasjonBevilgningFunksjonArt(),
                expectedSeverity = Severity.WARNING,
                ForAllRowItem(
                    description = "annually, isBevilgningRegnskap = false",
                    context = kostraRecordsInTest("0B", 850, 450, 1),
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, not isFylkeRegnskap = false",
                    context = kostraRecordsInTest("0C", 850, 450, 1),
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, funksjon != 850",
                    context = kostraRecordsInTest("0A", 100, 450, 1),
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, belop = 0",
                    context = kostraRecordsInTest("0A", 850, 100, 0),
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, all good",
                    context = kostraRecordsInTest("0A", 850, 450, 1),
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, all conditions match, 0A",
                    context = kostraRecordsInTest("0A", 850, 100, 1),
                    expectedErrorMessage =
                        "Det er kun artene 450, 810 og 850 som er logiske i kombinasjon med funksjon 850. " +
                            "Andre arter er ulogiske i kombinasjon med funksjon 850.",
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, all conditions match, 0I",
                    context = kostraRecordsInTest("0I", 850, 100, 1),
                    expectedErrorMessage =
                        "Det er kun artene 450, 810 og 850 som er logiske i kombinasjon med funksjon 850. " +
                            "Andre arter er ulogiske i kombinasjon med funksjon 850.",
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "annually, all conditions match, 0M",
                    context = kostraRecordsInTest("0M", 850, 100, 1),
                    expectedErrorMessage =
                        "Det er kun artene 450, 810 og 850 som er logiske i kombinasjon med funksjon 850. " +
                            "Andre arter er ulogiske i kombinasjon med funksjon 850.",
                    arguments = kostraArguments(" "),
                ),
                ForAllRowItem(
                    description = "1. quarter, all conditions match",
                    context = kostraRecordsInTest("0A", 850, 100, 1),
                    expectedErrorMessage =
                "Det er kun artene 450, 810 og 850 som er logiske i kombinasjon med funksjon 850. " +
                            "Andre arter er ulogiske i kombinasjon med funksjon 850.",
                    arguments = kostraArguments("1"),
                    expectedSeverity = Severity.WARNING,
                ),
                ForAllRowItem(
                    description = "4. quarter, all conditions match",
                    context = kostraRecordsInTest("0A", 850, 100, 1),
                    expectedErrorMessage =
                        "Det er kun artene 450, 810 og 850 som er logiske i kombinasjon med funksjon 850. " +
                            "Andre arter er ulogiske i kombinasjon med funksjon 850.",
                    arguments = kostraArguments("4"),
                    expectedSeverity = Severity.ERROR,
                ),
            ),
        )
    }) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            funksjon: Int,
            art: Int,
            belop: Int,
        ) = mapOf(
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_FUNKSJON to "$funksjon",
            RegnskapConstants.FIELD_ART to "$art",
            RegnskapConstants.FIELD_BELOP to "$belop",
        ).toKostraRecord(1, RegnskapFieldDefinitions.fieldDefinitions).asList()

        private fun kostraArguments(kvartal: String) = RuleTestData.argumentsInTest.copy(kvartal = kvartal)
    }
}

