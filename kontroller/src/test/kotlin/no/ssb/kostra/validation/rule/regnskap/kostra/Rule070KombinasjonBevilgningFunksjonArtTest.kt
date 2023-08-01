package no.ssb.kostra.validation.rule.regnskap.kostra

import io.kotest.core.spec.style.BehaviorSpec
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_ART
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_BELOP
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_FUNKSJON
import no.ssb.kostra.area.regnskap.RegnskapConstants.FIELD_SKJEMA
import no.ssb.kostra.area.regnskap.RegnskapFieldDefinitions.fieldDefinitions
import no.ssb.kostra.program.extension.asList
import no.ssb.kostra.program.extension.toKostraRecord
import no.ssb.kostra.validation.report.Severity
import no.ssb.kostra.validation.rule.ForAllRowItem
import no.ssb.kostra.validation.rule.KostraTestFactory.validationRuleTest

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
                    "art mismatch 530",
                    kostraRecordsInTest("0A", "899 ", "529", "1")
                ),
                ForAllRowItem(
                    "belop mismatch",
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
            FIELD_SKJEMA to skjema,
            FIELD_FUNKSJON to funksjon,
            FIELD_ART to art,
            FIELD_BELOP to belop,
        ).toKostraRecord(1, fieldDefinitions).asList()
    }
}