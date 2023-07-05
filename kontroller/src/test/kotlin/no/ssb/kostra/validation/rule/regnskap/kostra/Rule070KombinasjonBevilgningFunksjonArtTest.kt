package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.asList
import no.ssb.kostra.validation.rule.regnskap.RegnskapTestUtils.toKostraRecord

class Rule070KombinasjonBevilgningFunksjonArtTest : BehaviorSpec({
    include(
        validationRuleTest(
            sut = Rule070KombinasjonBevilgningFunksjonArt(),
            forAllRows = listOf(
                ForAllRowItem(
                    "bevilgningregnskap, funksjon, art og beløp matcher",
                    kostraRecordsInTest("0A", "899 ", "530", "1"),
                    expectedErrorMessage = "Art 530 er kun tillat brukt i kombinasjon med funksjon 880"
                ),
                ForAllRowItem(
                    "bevilgningregnskap = false",
                    kostraRecordsInTest("0X", "899 ", "530", "1")
                ),
                ForAllRowItem(
                    "funksjon matches 880",
                    kostraRecordsInTest("0A", "880 ", "530", "1")
                ),
                ForAllRowItem(
                    "art not matching 530",
                    kostraRecordsInTest("0A", "899 ", "529", "1")
                ),
                ForAllRowItem(
                    "belop matching 0",
                    kostraRecordsInTest("0A", "899 ", "530", "0")
                )
            ),
            expectedSeverity = Severity.ERROR,
            useArguments = false
        )
    )
}) {
    companion object {
        private fun kostraRecordsInTest(
            skjema: String,
            funksjon: String,
            art: String,
            belop: String,
        ) = mapOf(
            RegnskapConstants.FIELD_SKJEMA to skjema,
            RegnskapConstants.FIELD_FUNKSJON to funksjon,
            RegnskapConstants.FIELD_ART to art,
            RegnskapConstants.FIELD_BELOP to belop,
        ).toKostraRecord().asList()
    }
}